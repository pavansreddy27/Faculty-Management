import React from 'react';

const TimetablePage: React.FC = () => {
  return (
    <div className="container mx-auto p-6">
      <h1 className="text-3xl font-bold mb-6">My Timetable</h1>
      <p className="text-gray-700">
        This is where the faculty timetable will be displayed.
        You can fetch and render your schedule here.
      </p>
      {/* Add your timetable rendering logic here */}
      <div className="mt-8 p-4 border rounded-lg bg-white shadow-sm">
        <h2 className="text-xl font-semibold mb-3">Example Schedule:</h2>
        <ul className="list-disc pl-5">
          <li>Monday, 9:00 AM - 10:00 AM: CSC 101 - Introduction to CS</li>
          <li>Tuesday, 11:00 AM - 12:00 PM: MAT 203 - Calculus III (Office Hours)</li>
          <li>Wednesday, 2:00 PM - 3:30 PM: CSC 305 - Data Structures & Algorithms</li>
          <li>Friday, 10:00 AM - 11:00 AM: Research Meeting</li>
        </ul>
      </div>
    </div>
  );
};

export default TimetablePage; // <<<--- THIS IS THE CRUCIAL LINE