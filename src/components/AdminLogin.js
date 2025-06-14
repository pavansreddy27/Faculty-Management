// src/components/AdminLogin.js
import React, { useState } from 'react'
import axios from '../axiosInstance'
import { useNavigate } from 'react-router-dom'

export default function AdminLogin() {
  const [u,p,e] = [useState(''),useState(''),useState('')]
  const [error,setError]=e
  const navigate = useNavigate()

  const submit = async (evt) => {
    evt.preventDefault()
    try {
      const res = await axios.post('/admin/login', { username: u[0], password: p[0] })
      localStorage.setItem('adminToken', res.data.token)
      navigate('/admin/faculty')
    } catch {
      setError('Invalid credentials')
    }
  }

  return (
    <div className="container mt-5">
      <h2>Admin Login</h2>
      {error && <div className="alert alert-danger">{error}</div>}
      <form onSubmit={submit}>
        <div className="mb-3">
          <label>Username</label>
          <input className="form-control" value={u[0]} onChange={e=>u[1](e.target.value)} />
        </div>
        <div className="mb-3">
          <label>Password</label>
          <input type="password" className="form-control" value={p[0]} onChange={e=>p[1](e.target.value)} />
        </div>
        <button className="btn btn-primary">Login</button>
      </form>
    </div>
  )
}
