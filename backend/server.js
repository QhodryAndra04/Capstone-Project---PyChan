require('dotenv').config();
const express = require('express');
const bodyParser = require('body-parser');
const dotenv = require('dotenv');
const authRoutes = require('./routes/authRoutes');
const authenticateToken = require('./middlewares/authenticateToken');

// Load environment variables
dotenv.config();

// Initialize app
const app = express();

// Middleware
app.use(bodyParser.json());

// Routes
app.use('/api/auth', authRoutes);

// Route default untuk /
app.get('/', (req, res) => {
  res.send('Welcome to the API');
});

// Start server
const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});