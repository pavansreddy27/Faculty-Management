// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, Outlet } from 'react-router-dom';

import AdminLogin from './components/AdminLogin';
import FacultyLogin from './components/FacultyLogin';
import Navbar from './components/Navbar';
import FacultyList from './components/FacultyList';
import FacultyForm from './components/FacultyForm';
import Timetable from './components/Timetable';
import LoginPage from './components/LoginPage'; // Import LoginPage

const AdminGuard = () => {
  return localStorage.getItem('adminToken')
    ? <><Navbar /><Outlet/></>
    : <Navigate to="/admin/login"/>;
};

const FacultyGuard = () => {
  return localStorage.getItem('facultyToken')
    ? <Outlet/>
    : <Navigate to="/faculty/login"/>;
};

export default function App() {
  return (
    <Router>
      <Routes>
        {/* Login selection page */}
        <Route path="/login" element={<LoginPage />} />

        {/* Admin login & admin-only routes */}
        <Route path="/admin/login" element={<AdminLogin />} />
        <Route element={<AdminGuard/>}>
          <Route path="/admin/faculty" element={<FacultyList/>}/>
          <Route path="/admin/faculty/add" element={<FacultyForm/>}/>
          <Route path="/admin/faculty/edit/:id" element={<FacultyForm/>}/>
        </Route>

        {/* Faculty login & timetable */}
        <Route path="/faculty/login" element={<FacultyLogin />} />
        <Route element={<FacultyGuard/>}>
          <Route path="/faculty/timetable" element={<Timetable/>}/>
        </Route>

        {/* catch-all - redirect to the login choice page */}
        <Route path="*" element={<Navigate to="/login"/>}/>
      </Routes>
    </Router>
  );
}