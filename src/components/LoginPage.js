// src/components/LoginPage.js
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './LoginPage.css';

function LoginPage() {
  const navigate = useNavigate();

  const handleAdminLoginClick = () => {
    console.log('Admin Login button clicked');
    navigate('/admin/login'); // Navigate to the admin login page
  };

  const handleFacultyLoginClick = () => {
    console.log('Faculty Login button clicked');
    navigate('/faculty/login'); // Navigate to the faculty login page
  };

  return (
    <div className="login-page-container">
      <h1>Choose Login Type</h1>
      <div className="login-buttons-container">
        <button className="admin-login-button" onClick={handleAdminLoginClick}>
          Admin Login
        </button>
        <button className="faculty-login-button" onClick={handleFacultyLoginClick}>
          Faculty Login
        </button>
      </div>
    </div>
  );
}

export default LoginPage;