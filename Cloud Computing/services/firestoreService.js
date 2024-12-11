const { Firestore } = require('@google-cloud/firestore');
const path = require('path');

// Inisialisasi Firestore
const firestore = new Firestore({
  projectId: 'phychan-cp-443016', // Ganti dengan Project ID Anda
  keyFilename: path.join(__dirname, '../serviceAccountKey.json'), // Path ke service account key
});

// Referensi ke koleksi `users`
const usersCollection = firestore.collection('users');

// Fungsi untuk menambahkan pengguna
const addUser = async (userData) => {
  try {
    const userRef = await usersCollection.add(userData);
    console.log(`User added with ID: ${userRef.id}`);
    return userRef.id;
  } catch (error) {
    console.error('Error adding user:', error.message);
    throw new Error('Failed to add user');
  }
};

// Fungsi untuk mencari pengguna berdasarkan email
const findUserByEmail = async (email) => {
  try {
    const userSnapshot = await usersCollection.where('email', '==', email).get();

    if (userSnapshot.empty) {
      return null; // Tidak ada pengguna dengan email ini
    }

    const userDoc = userSnapshot.docs[0]; // Ambil dokumen pertama
    return { id: userDoc.id, ...userDoc.data() }; // Ambil ID dokumen dan datanya
  } catch (error) {
    console.error('Error finding user by email:', error.message);
    throw new Error('Failed to find user by email');
  }
};

// Fungsi untuk mencari pengguna berdasarkan ID
const findUserById = async (userId) => {
  try {
    const userDoc = await usersCollection.doc(userId).get();
    if (!userDoc.exists) {
      return null;
    }
    return { id: userDoc.id, ...userDoc.data() };
  } catch (error) {
    console.error('Error finding user by ID:', error.message);
    throw new Error('Failed to find user by ID');
  }
};

// Fungsi untuk memperbarui data pengguna
const updateUser = async (userId, updates) => {
  try {
    const userDocRef = usersCollection.doc(userId);
    await userDocRef.update(updates);
    console.log(`User updated with ID: ${userId}`);
  } catch (error) {
    console.error('Error updating user:', error.message);
    throw new Error('Failed to update user');
  }
};

// Fungsi untuk menghapus pengguna berdasarkan ID
const deleteUser = async (userId) => {
  try {
    const userDocRef = usersCollection.doc(userId);
    const userDoc = await userDocRef.get();

    if (!userDoc.exists) {
      throw new Error('User not found');
    }

    await userDocRef.delete();
    console.log(`User with ID ${userId} deleted successfully.`);
  } catch (error) {
    console.error('Error deleting user:', error.message);
    throw new Error('Failed to delete user');
  }
};

// Ekspor fungsi
module.exports = { addUser, findUserByEmail, findUserById, updateUser, deleteUser, firestore, usersCollection };
