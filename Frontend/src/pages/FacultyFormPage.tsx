import React, { useState, useEffect } from 'react';
import { Button } from '../components/ui/button';
import { Input } from '../components/ui/input';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '../components/ui/card';
import { Label } from '../components/ui/label';
import { toast } from 'react-hot-toast';
import { useNavigate, useParams } from 'react-router-dom';
import { facultyService, departmentService } from '../api/services';
// Import all necessary types, including the new FacultyCreationRequest
import { FacultyProfile, User, Department, Role, FacultyProfileRequest, FacultyCreationRequest as ClientFacultyCreationRequest } from '../types';
import { ChevronLeft } from 'lucide-react';


// Define a union type for formData state to accommodate both add and edit scenarios
// For 'add' mode, we need username, email, password from FacultyCreationRequest
// For 'edit' mode, we have a full FacultyProfile
type FacultyFormState = FacultyProfile & {
    username?: string; // Only for new user creation
    password?: string; // Only for new user creation
    // email is already part of formData.user.email
};

const FacultyFormPage: React.FC = () => {
    const navigate = useNavigate();
    const { id } = useParams<{ id: string }>();
    const isEditMode = !!id;

    // Initial state for formData
    const [formData, setFormData] = useState<FacultyFormState>({
        // FacultyProfile fields
        id: 0,
        firstName: '',
        lastName: '',
        phone: '',
        officeLocation: '',
        hireDate: '',
        bio: '',
        profilePictureUrl: '',
        createdAt: '', // Mandatory FacultyProfile field
        updatedAt: '', // Mandatory FacultyProfile field
        department: {
            id: 0,
            name: '',
            description: '', // Optional in Department, initialize if needed
            createdAt: '',   // Mandatory in Department
            updatedAt: ''    // Mandatory in Department
        },
        user: { // User is mandatory in FacultyProfile
            id: 0,
            username: '',
            email: '',
            roles: [],       // Mandatory in User
            createdAt: '',   // Mandatory in User
            updatedAt: ''    // Mandatory in User
        },
        // Fields specific to new user creation (only for isEditMode === false)
        username: '', // Initialize for potential new user
        password: ''  // Initialize for potential new user
    });

    const [departments, setDepartments] = useState<Department[]>([]);
    const [loading, setLoading] = useState(false);
    const [loadingData, setLoadingData] = useState(isEditMode);

    useEffect(() => {
        // Fetch departments for the dropdown
        const fetchDepartments = async () => {
            try {
                const response = await departmentService.getAll();
                if (response.data.success) {
                    setDepartments(response.data.data);
                }
            } catch (error) {
                toast.error('Failed to load departments.');
            }
        };
        fetchDepartments();

        // If in edit mode, fetch existing faculty data
        if (isEditMode) {
            const fetchFaculty = async () => {
                try {
                    const response = await facultyService.getById(Number(id));
                    if (response.data.success) {
                        const facultyData = response.data.data;

                        setFormData({
                            id: facultyData.id,
                            firstName: facultyData.firstName,
                            lastName: facultyData.lastName,
                            phone: facultyData.phone || '',
                            officeLocation: facultyData.officeLocation || '',
                            hireDate: facultyData.hireDate ? new Date(facultyData.hireDate).toISOString().split('T')[0] : '',
                            bio: facultyData.bio || '',
                            profilePictureUrl: facultyData.profilePictureUrl || '',
                            department: facultyData.department ? {
                                id: facultyData.department.id,
                                name: facultyData.department.name,
                                description: facultyData.department.description || '',
                                createdAt: facultyData.department.createdAt,
                                updatedAt: facultyData.department.updatedAt
                            } : { // Default empty department if none exists or if it's null
                                id: 0, name: '', description: '', createdAt: '', updatedAt: ''
                            },
                            user: facultyData.user ? {
                                id: facultyData.user.id,
                                username: facultyData.user.username,
                                email: facultyData.user.email,
                                roles: facultyData.user.roles || [],
                                createdAt: facultyData.user.createdAt,
                                updatedAt: facultyData.user.updatedAt
                            } : { // Fallback if user somehow missing (though type says mandatory)
                                id: 0, username: '', email: '', roles: [], createdAt: '', updatedAt: ''
                            },
                            createdAt: facultyData.createdAt,
                            updatedAt: facultyData.updatedAt,
                            // Do NOT set username and password for editing existing faculty
                            username: undefined, // Explicitly set to undefined for edit mode
                            password: undefined  // Explicitly set to undefined for edit mode
                        });
                    } else {
                        toast.error('Faculty not found.');
                        navigate('/faculty');
                    }
                } catch (error) {
                    toast.error('Failed to load faculty data for editing.');
                    navigate('/faculty');
                } finally {
                    setLoadingData(false);
                }
            };
            fetchFaculty();
        } else {
            setLoadingData(false);
        }
    }, [id, isEditMode, navigate]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = e.target;

        if (name === 'departmentId') {
            const selectedDepartment = departments.find(dep => dep.id === Number(value));
            setFormData(prev => ({
                ...prev,
                department: selectedDepartment ? {
                    ...selectedDepartment,
                    description: selectedDepartment.description || '',
                    createdAt: selectedDepartment.createdAt || '',
                    updatedAt: selectedDepartment.updatedAt || ''
                } : { id: 0, name: '', description: '', createdAt: '', updatedAt: '' }
            }));
        } else if (name === 'email') {
            setFormData(prev => ({
                ...prev,
                user: { ...prev.user, email: value }
            }));
        } else if (name === 'username' || name === 'password') {
            // Handle username and password directly on formData for new faculty
            setFormData(prev => ({ ...prev, [name]: value }));
        } else {
            setFormData(prev => ({ ...prev, [name]: value }));
        }
    };


    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);

        try {
            let response;

            if (isEditMode) {
                // For editing, use FacultyProfileRequest
                const dataToSend: FacultyProfileRequest = {
                    firstName: formData.firstName,
                    lastName: formData.lastName,
                    bio: formData.bio,
                    profilePictureUrl: formData.profilePictureUrl,
                    departmentId: formData.department?.id || undefined,
                    phone: formData.phone,
                    officeLocation: formData.officeLocation,
                    hireDate: formData.hireDate, // Assuming backend handles empty string or null for Optional LocalDate
                    // userId: formData.user.id, // Only include if backend FacultyProfileRequest explicitly expects this for update
                };
                response = await facultyService.update(Number(id), dataToSend);
                toast.success('Faculty profile updated successfully!');
            } else {
                // For creating, use FacultyCreationRequest
                const dataToSend: ClientFacultyCreationRequest = {
                    username: formData.username || '', // Mandatory
                    email: formData.user.email,       // Mandatory, from nested user
                    password: formData.password || '', // Mandatory
                    firstName: formData.firstName,
                    lastName: formData.lastName,
                    bio: formData.bio,
                    profilePictureUrl: formData.profilePictureUrl,
                    departmentId: formData.department?.id || 0, // Mandatory, ensure it's not undefined for NotNull
                    phone: formData.phone,
                    officeLocation: formData.officeLocation,
                    hireDate: formData.hireDate ? (formData.hireDate) : null // Backend expects LocalDate (null for optional)
                };

                // Basic client-side validation for mandatory fields in creation
                if (!dataToSend.username || !dataToSend.email || !dataToSend.password || !dataToSend.firstName || !dataToSend.lastName || !dataToSend.departmentId) {
                    toast.error('Please fill in all required fields (username, email, password, first name, last name, department).');
                    setLoading(false);
                    return;
                }
                if (dataToSend.departmentId === 0) {
                     toast.error('Please select a department.');
                     setLoading(false);
                     return;
                }


                response = await facultyService.create(dataToSend); // Now sends FacultyCreationRequest structure
                toast.success('Faculty profile and user created successfully!');
            }

            if (response.data.success) {
                navigate('/faculty');
            } else {
                toast.error(response.data.message || 'Operation failed.');
            }
        } catch (error: any) {
            if (error.response && error.response.data && error.response.data.message) {
                toast.error(error.response.data.message);
            } else if (error.response && error.response.status === 400) {
                toast.error('Bad Request: Please check your input.');
            } else {
                toast.error('An error occurred during submission.');
            }
            console.error("Submission error:", error); // Log the full error
        } finally {
            setLoading(false);
        }
    };

    if (loadingData) {
        return (
            <div className="flex items-center justify-center h-64">
                <div className="text-lg text-gray-600">Loading faculty data...</div>
            </div>
        );
    }

    return (
        <div className="space-y-6">
            <div className="flex items-center justify-between">
                <Button variant="ghost" onClick={() => navigate(-1)} className="mb-4">
                    <ChevronLeft className="w-5 h-5 mr-2" /> Back
                </Button>
            </div>
            <Card>
                <CardHeader>
                    <CardTitle>{isEditMode ? 'Edit Faculty Profile' : 'Add New Faculty'}</CardTitle>
                    <CardDescription>
                        {isEditMode
                            ? `Update details for ${formData.firstName} ${formData.lastName}.`
                            : 'Fill in the details to add a new faculty member and create an associated user account.'}
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-6">
                        {/* User Creation Fields (only for Add mode) */}
                        {!isEditMode && (
                            <>
                                <div>
                                    <Label htmlFor="username">Username</Label>
                                    <Input
                                        id="username"
                                        name="username"
                                        value={formData.username || ''}
                                        onChange={handleChange}
                                        placeholder="Enter username for login"
                                        required
                                    />
                                </div>
                                <div>
                                    <Label htmlFor="password">Password</Label>
                                    <Input
                                        id="password"
                                        name="password"
                                        type="password"
                                        value={formData.password || ''}
                                        onChange={handleChange}
                                        placeholder="Enter password"
                                        required
                                    />
                                </div>
                            </>
                        )}

                        {/* Faculty Profile Fields */}
                        {/* Email - now correctly mapped to user.email, visible in both modes */}
                        <div>
                            <Label htmlFor="email">Email</Label>
                            <Input
                                id="email"
                                name="email"
                                type="email"
                                value={formData.user.email} // Access email from nested user object
                                onChange={handleChange}
                                placeholder="Enter email address"
                                required
                            />
                        </div>

                        <div>
                            <Label htmlFor="firstName">First Name</Label>
                            <Input
                                id="firstName"
                                name="firstName"
                                value={formData.firstName}
                                onChange={handleChange}
                                placeholder="Enter first name"
                                required
                            />
                        </div>
                        <div>
                            <Label htmlFor="lastName">Last Name</Label>
                            <Input
                                id="lastName"
                                name="lastName"
                                value={formData.lastName}
                                onChange={handleChange}
                                placeholder="Enter last name"
                                required
                            />
                        </div>
                        <div>
                            <Label htmlFor="phone">Phone</Label>
                            <Input
                                id="phone"
                                name="phone"
                                type="tel"
                                value={formData.phone || ''}
                                onChange={handleChange}
                                placeholder="Enter phone number"
                            />
                        </div>
                        <div>
                            <Label htmlFor="departmentId">Department</Label>
                            <select
                                id="departmentId"
                                name="departmentId"
                                value={formData.department?.id || 0}
                                onChange={handleChange}
                                className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
                                required={!isEditMode} // Department is required for new faculty, but might be optional for existing (if allowed by backend)
                            >
                                <option value="">Select a Department</option>
                                {departments.map((dep) => (
                                    <option key={dep.id} value={dep.id}>
                                        {dep.name}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div>
                            <Label htmlFor="officeLocation">Office Location</Label>
                            <Input
                                id="officeLocation"
                                name="officeLocation"
                                value={formData.officeLocation || ''}
                                onChange={handleChange}
                                placeholder="e.g., Building A, Room 101"
                            />
                        </div>
                        <div>
                            <Label htmlFor="hireDate">Hire Date</Label>
                            <Input
                                id="hireDate"
                                name="hireDate"
                                type="date"
                                value={formData.hireDate || ''}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className="md:col-span-2">
                            <Label htmlFor="bio">Biography</Label>
                            <textarea
                                id="bio"
                                name="bio"
                                value={formData.bio || ''}
                                onChange={handleChange}
                                rows={4}
                                className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                                placeholder="Enter a brief biography"
                            ></textarea>
                        </div>
                        <div>
                            <Label htmlFor="profilePictureUrl">Profile Picture URL</Label>
                            <Input
                                id="profilePictureUrl"
                                name="profilePictureUrl"
                                value={formData.profilePictureUrl || ''}
                                onChange={handleChange}
                                placeholder="Enter URL for profile picture"
                            />
                        </div>


                        {/* Submit Button */}
                        <div className="md:col-span-2 flex justify-end">
                            <Button type="submit" disabled={loading} className="bg-indigo-600 hover:bg-indigo-700">
                                {loading ? 'Saving...' : (isEditMode ? 'Update Faculty' : 'Add Faculty')}
                            </Button>
                        </div>
                    </form>
                </CardContent>
            </Card>
        </div>
    );
};

export default FacultyFormPage;