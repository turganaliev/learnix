import React, { useState } from 'react';
import api from './api';
import { Link, useNavigate } from 'react-router-dom';

function Register() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    password: ''
  });
  const [errors, setErrors] = useState({});

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/users/register', formData);
      navigate('/login', { state: { message: 'Account created. Please sign in.'} });
    } catch (error) {
      console.error('Error:', error);
      if (error.response && error.response.data) {
        setErrors(error.response.data);
      } else {
        alert('Registration failed!');
      }
    }
  };

    return (
      <div className="auth-page">
        <div className="auth-card">
          <h1>Create your account</h1>
          <p className="auth-sub">Start asking questions about what you're studying.</p>

          <form onSubmit={handleSubmit}>
            <input name="firstName" placeholder="First name" onChange={handleChange} />
            <input name="lastName" placeholder="Last name" onChange={handleChange} />
            <input name="username" placeholder="Username" onChange={handleChange} />
            <input name="email" placeholder="Email" onChange={handleChange} />
            <input name="password" type="password" placeholder="Password" onChange={handleChange} />
            <button type="submit">Create account</button>
          </form>
          <p className="auth-alt">
            Already have an account? <Link to="/login">Sign in</Link>
          </p>

          {Object.entries(errors).map(([field, message]) => (
            <p key={field} className="field-error">{message}</p>
          ))}
        </div>
      </div>
    );
}

export default Register;