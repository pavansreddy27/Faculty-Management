import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axios from '../axiosInstance';

// FacultyForm Component (Handles Add and Edit)
function FacultyForm() {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [dept, setDept] = useState('');
    const [qualification, setQualification] = useState('');
    const [specialization, setSpecialization] = useState('');
    const [experience, setExperience] = useState('');
    const [joiningDate, setJoiningDate] = useState('');
    const [contactNumber, setContactNumber] = useState('');
    const [role, setRole] = useState(''); // Changed from rank to role
    const [registrationNumber, setRegistrationNumber] = useState('');
    const nav = useNavigate(), { id } = useParams();

    useEffect(() => {
        if (id) {
            axios.get(`/faculty/registrationNumber/${id}`).then(r => {
                setName(r.data.name);
                setEmail(r.data.email);
                setDept(r.data.department);
                setQualification(r.data.qualification);
                setSpecialization(r.data.specialization);
                setExperience(r.data.experience);
                setJoiningDate(r.data.joiningDate);
                setContactNumber(r.data.contactNumber);
                setRole(r.data.role); // Changed from rank to role
                setRegistrationNumber(r.data.registrationNumber);
            });
        } else {
            const year = joiningDate ? new Date(joiningDate).getFullYear() : new Date().getFullYear();
            let numericalRole;
            switch (role) { // Changed from rank to role
                case 'AP':
                    numericalRole = 1;
                    break;
                case 'P':
                    numericalRole = 2;
                    break;
                case 'HOD':
                    numericalRole = 3;
                    break;
                default:
                    numericalRole = 0;
            }
            const sequentialNumber = '001';
            setRegistrationNumber(`${year}${numericalRole}${sequentialNumber}`);
        }
    }, [id, joiningDate, role]); // Changed from rank to role

    const handleRoleChange = (e) => {
        setRole(e.target.value); // Changed from setRank to setRole
        const year = joiningDate ? new Date(joiningDate).getFullYear() : new Date().getFullYear();
        let numericalRole;
        switch (e.target.value) { // Changed from e.target.value
            case 'AP':
                numericalRole = 1;
                break;
            case 'P':
                numericalRole = 2;
                break;
            case 'HOD':
                numericalRole = 3;
                break;
            default:
                numericalRole = 0;
        }
        const sequentialNumber = '001';
        setRegistrationNumber(`${year}${numericalRole}${sequentialNumber}`);
    }

    const save = (e) => {
    e.preventDefault();
    const payload = {
        name,
        email,
        department: dept,
        qualification,
        specialization,
        experience,
        joiningDate,
        contactNumber,
        role,
        registrationNumber,
    };
    const req = id
        ? axios.put(`/faculty/registrationNumber/${id}`, payload) //  id should be registrationNumber
        : axios.post('/faculty', payload);
    req.then((_) => nav('/admin/faculty'));
};


    return (
        <div className="container mt-4">
            <h2>{id ? 'Edit' : 'Add'} Faculty</h2>
            <form onSubmit={save}>
                <div className="mb-3">
                    <label>Name</label>
                    <input className="form-control" value={name} onChange={e => setName(e.target.value)} />
                </div>
                <div className="mb-3">
                    <label>Email</label>
                    <input className="form-control" value={email} onChange={e => setEmail(e.target.value)} />
                </div>
                <div className="mb-3">
                    <label>Department</label>
                    <input className="form-control" value={dept} onChange={e => setDept(e.target.value)} />
                </div>
                <div className="mb-3">
                    <label>Qualification</label>
                    <input className="form-control" value={qualification} onChange={e => setQualification(e.target.value)} />
                </div>
                <div className="mb-3">
                    <label>Specialization</label>
                    <input className="form-control" value={specialization} onChange={e => setSpecialization(e.target.value)} />
                </div>
                <div className="mb-3">
                    <label>Experience (Years)</label>
                    <input type="number" className="form-control" value={experience} onChange={e => setExperience(e.target.value)} />
                </div>
                <div className="mb-3">
                    <label>Joining Date</label>
                    <input type="date" className="form-control" value={joiningDate} onChange={e => setJoiningDate(e.target.value)} />
                </div>
                <div className="mb-3">
                    <label>Contact Number</label>
                    <input
                        type="tel"
                        className="form-control"
                        value={contactNumber}
                        onChange={(e) => setContactNumber(e.target.value)}
                    />
                </div>
                <div className="mb-3">
                    <label>Role</label> {/* Changed from Rank to Role */}
                    <select
                        className="form-select"
                        value={role} // Changed from rank to role
                        onChange={handleRoleChange} // Changed from handleRankChange to handleRoleChange
                    >
                        <option value="">Select Role</option> {/* Changed from Select Rank to Select Role */}
                        <option value="AP">Assistant Professor</option>
                        <option value="P">Professor</option>
                        <option value="HOD">Head of Department</option>
                    </select>
                </div>
                <div className="mb-3">
                    <label>Registration Number</label>
                    <input
                        type="text"
                        className="form-control"
                        value={registrationNumber}
                        readOnly
                    />
                </div>
                <button className="btn btn-success">{id ? 'Update' : 'Create'}</button>
            </form>
        </div>
    );
}

export default FacultyForm;
