const jwt = require('jwt-simple');
const dotenv = require('dotenv');

dotenv.config();

// Middleware untuk memverifikasi token
const authenticateToken = (req, res, next) => {
  const token = req.header('Authorization')?.split(' ')[1]; // Bearer <token>

  if (!token) {
    return res.status(403).json({ message: 'Access denied. No token provided.' });
  }

  try {
    const decoded = jwt.decode(token, process.env.JWT_SECRET);
    req.user = decoded;  // Menyimpan data user yang didekodekan dari token
    next();
  } catch (error) {
    return res.status(400).json({ message: 'Invalid or expired token.' });
  }
};

module.exports = authenticateToken;