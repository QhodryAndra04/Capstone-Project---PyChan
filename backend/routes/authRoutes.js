const express = require('express');
const router = express.Router();
const authenticateToken = require('../middlewares/authenticateToken');
const authController = require('../controllers/authController');

// Endpoint untuk registrasi
router.post('/register', authController.register);

// Endpoint untuk login
router.post('/login', authController.login);

// Endpoint untuk forget password (reset password)
router.post('/forget-password', authController.forgetPassword);

// Endpoint untuk change password
router.post('/change-password', authenticateToken, authController.changePassword);

// Endpoint untuk menghapus akun
router.delete('/delete-account', authenticateToken, authController.deleteAccount);

// Endpoint untuk logout
router.post('/logout', authController.logout);

module.exports = router;
