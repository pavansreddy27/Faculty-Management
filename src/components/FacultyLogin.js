// src/components/FacultyLogin.js
import React, { useState } from 'react'
import axios from '../axiosInstance'
import { useNavigate } from 'react-router-dom'

export default function FacultyLogin() {
  const [u,p,e] = [useState(''),useState(''),useState('')]
  const navigate = useNavigate()

  const submit = async (evt) => {
    evt.preventDefault()
    try {
      const res = await axios.post('/admin/login', { username: u[0], password: p[0] })
      // assume same admin endpoint returns facultyToken on faculty credentials
      localStorage.setItem('facultyToken', res.data.token)
      navigate('/faculty/timetable')
    } catch {
      e[1]('Invalid credentials')
    }
  }

  return (
    <div className="container mt-5">
      <h2>Faculty Login</h2>
      {e[0] && <div className="alert alert-danger">{e[0]}</div>}
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
