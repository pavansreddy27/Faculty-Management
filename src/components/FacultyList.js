import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Trash2, Edit } from 'lucide-react';
import axios from '../axiosInstance';

const FacultyList = () => {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('/faculty');
                setData(response.data);
            } catch (err) {
                setError(err.message || 'Failed to fetch faculty data.');
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, []);

    const del = async (id) => { // Removed : number
        if (!window.confirm('Are you sure you want to delete this faculty member?')) return;
        try {
            await axios.delete(`/faculty/${id}`);
            setData(prevData => prevData.filter(faculty => faculty.id !== id));
        } catch (err) {
            setError(err.message || 'Failed to delete faculty member.');
        }
    };

    if (loading) {
        return <div className="container mt-4">Loading faculty list...</div>;
    }

    if (error) {
        return <div className="container mt-4 text-red-500">Error: {error}</div>;
    }

    return (
        <div className="container mt-4">
            <h2>Faculty</h2>
            <table className="table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Department</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {data.length === 0 ? (
                        <tr>
                            <td colSpan={4} className="text-center">No faculty data available.</td >
                        </tr>
                    ) : (
                        data.map(faculty => (
                            <tr key={faculty.id}>
                                <td>{faculty.name}</td>
                                <td>{faculty.email}</td>
                                <td>{faculty.department}</td>
                                <td>
                                    <Link
                                        className="btn btn-sm btn-warning me-2"
                                        to={`/admin/faculty/edit/${faculty.id}`}
                                    >
                                        <Edit className="mr-2 h-4 w-4" /> Edit
                                    </Link>
                                    <button
                                        className="btn btn-sm btn-danger"
                                        onClick={() => del(faculty.id)} // Removed : number
                                    >
                                        <Trash2 className="mr-2 h-4 w-4" /> Delete
                                    </button>
                                </td>
                            </tr>
                        ))
                    )}
                </tbody>
            </table>
            {data.length > 0 && (
                <div className="mt-4 text-sm text-gray-500">
                    Total Faculty: {data.length}
                </div>
            )}
        </div>
    );
};

export default FacultyList;
