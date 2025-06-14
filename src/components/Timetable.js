// src/components/Timetable.js
import React, { useState, useEffect } from 'react'
import axios from '../axiosInstance'

export default function Timetable(){
  const [tt,setTt]=useState([])
  useEffect(()=>{
    axios.get('/timetable') // assumes your backend reads token to know user
      .then(r=>setTt(r.data))
  },[])
  return (
    <div className="container mt-4">
      <h2>Your Timetable</h2>
      <table className="table">
        <thead><tr><th>Day</th><th>Time</th><th>Subject</th></tr></thead>
        <tbody>
          {tt.map(x=>(
            <tr key={x.id}>
              <td>{x.day}</td>
              <td>{x.time}</td>
              <td>{x.subject}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
