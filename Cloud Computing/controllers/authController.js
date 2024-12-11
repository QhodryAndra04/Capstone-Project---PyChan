const jwt = require('jsonwebtoken');
const { usersCollection } = require('../services/firestoreService');
const { findUserByEmail, addUser, deleteUser } = require('../services/firestoreService');

// Fungsi untuk registrasi pengguna
const register = async (req, res) => {
  const { username, email, password, confirmPassword } = req.body;

  if (password !== confirmPassword) {
    return res.status(400).json({ message: 'Passwords do not match.' });
  }

  try {
    const existingUser = await findUserByEmail(email);
    if (existingUser) {
      return res.status(409).json({ message: 'User already exists.' });
    }

    const userData = { username, email, password };
    const userId = await addUser(userData);

    return res.status(201).json({ message: 'User registered successfully.', userId });
  } catch (error) {
    console.error('Error registering user:', error.message);
    return res.status(500).json({ message: 'Internal Server Error' });
  }
};

// Fungsi untuk login pengguna
const login = async (req, res) => {
  const { email, password } = req.body;

  // Validasi input
  if (!email || !password) {
    return res.status(400).json({
      error: true,
      message: 'Email and password are required.',
    });
  }

  try {
    // Cari user berdasarkan email
    const user = await findUserByEmail(email);

    if (!user) {
      return res.status(404).json({
        error: true,
        message: 'User not found.',
      });
    }

    // Verifikasi password
    if (user.password === password) {
      // Generate JWT Token
      const token = jwt.sign(
        { userId: user.id, username: user.username }, // Payload token
        process.env.JWT_SECRET, // Secret key
        { expiresIn: '1h' } // Masa berlaku token
      );

      // Respon sukses dengan tambahan email
      return res.status(200).json({
        error: false,
        message: 'success',
        loginResult: {
          userId: user.id,
          name: user.username,
          email: user.email, 
          password : user.password,
          token: token,
        },
      });
    } else {
      return res.status(400).json({
        error: true,
        message: 'Invalid password.',
      });
    }
  } catch (error) {
    console.error('Error logging in:', error.message);
    return res.status(500).json({
      error: true,
      message: 'Internal Server Error',
    });
  }
};

// Fungsi untuk reset password (forget password)
const forgetPassword = async (req, res) => {
  const { email, newPassword, confirmPassword } = req.body;

  if (!email || !newPassword || !confirmPassword) {
    return res.status(400).json({ message: 'Email, new password, and confirm password are required.' });
  }

  if (newPassword !== confirmPassword) {
    return res.status(400).json({ message: 'Passwords do not match.' });
  }

  try {
    // Cari pengguna berdasarkan email
    const userSnapshot = await usersCollection.where('email', '==', email).get();

    if (userSnapshot.empty) {
      return res.status(404).json({ message: 'User not found.' });
    }

    const userDoc = userSnapshot.docs[0];
    const userId = userDoc.id;

    // Perbarui password pengguna di Firestore
    await usersCollection.doc(userId).update({ password: newPassword });

    return res.status(200).json({ message: 'Password updated successfully.' });
  } catch (error) {
    console.error('Error resetting password:', error.message);
    return res.status(500).json({ message: 'Error resetting password.' });
  }
};

// Fungsi untuk mengubah password
const changePassword = async (req, res) => {
  const { userId, oldPassword, newPassword, confirmPassword } = req.body;

  if (!userId || !oldPassword || !newPassword || !confirmPassword) {
    return res.status(400).json({ message: 'User ID, old password, new password, and confirm password are required.' });
  }

  if (newPassword !== confirmPassword) {
    return res.status(400).json({ message: 'New passwords do not match.' });
  }

  try {
    // Cari pengguna berdasarkan userId
    const userDocRef = usersCollection.doc(userId);
    const userDoc = await userDocRef.get();

    if (!userDoc.exists) {
      return res.status(404).json({ message: 'User not found.' });
    }

    const userData = userDoc.data();

    // Verifikasi password lama
    if (userData.password !== oldPassword) {
      return res.status(400).json({ message: 'Old password is incorrect.' });
    }

    // Perbarui password dengan yang baru
    await userDocRef.update({ password: newPassword });

    return res.status(200).json({ message: 'Password changed successfully.' });
  } catch (error) {
    console.error('Error changing password:', error.message);
    return res.status(500).json({ message: 'Error changing password.' });
  }
};

// Fungsi untuk menghapus akun pengguna
const deleteAccount = async (req, res) => {
  const { userId } = req.body;

  if (req.user.userId !== userId) {
    return res.status(403).json({ message: 'Forbidden: Cannot delete another user\'s account.' });
  }

  try {
    await deleteUser(userId);
    return res.status(200).json({ message: 'Account deleted successfully.' });
  } catch (error) {
    console.error('Error deleting account:', error.message);
    return res.status(500).json({ message: 'Error deleting account.' });
  }
};

// Fungsi untuk logout
const logout = async (req, res) => {
  res.clearCookie('token'); // Jika token disimpan di cookies
  
  return res.status(200).json({ message: 'Logged out successfully' });
};

module.exports = { register, login, changePassword, deleteAccount, forgetPassword, logout };
