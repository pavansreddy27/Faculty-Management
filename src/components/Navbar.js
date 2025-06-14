// src/components/Navbar.js
import React from 'react'
import { NavLink, useNavigate } from 'react-router-dom'

export default function Navbar(){
  const nav = useNavigate()
  const logout = () => {
    localStorage.removeItem('adminToken')
    nav('/admin/login')
  }
  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
      <div className="container">
        <NavLink className="navbar-brand" to="/admin/faculty">FacultyAdmin</NavLink>
        <div className="collapse navbar-collapse">
          <ul className="navbar-nav me-auto">
            <li className="nav-item"><NavLink className="nav-link" to="/admin/faculty">List</NavLink></li>
            <li className="nav-item"><NavLink className="nav-link" to="/admin/faculty/add">Add</NavLink></li>
          </ul>
          <button className="btn btn-outline-light" onClick={logout}>Logout</button>
        </div>
      </div>
    </nav>
  )
}
