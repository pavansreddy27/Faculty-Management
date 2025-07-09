// src/pages/SettingsPage.tsx
import React from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card';
import { Button } from '../components/ui/button';
import { Input } from '../components/ui/input'; // Assuming you have an Input component
import { Label } from '../components/ui/label'; // Assuming you have a Label component
import { toast } from 'react-hot-toast'; // For notifications

const SettingsPage: React.FC = () => {
    // Example state for settings fields
    const [emailNotifications, setEmailNotifications] = React.useState(true);
    const [theme, setTheme] = React.useState('light'); // 'light' or 'dark'
    const [password, setPassword] = React.useState('');
    const [confirmPassword, setConfirmPassword] = React.useState('');

    const handleSaveChanges = () => {
        // Here you would typically send these settings to your backend API
        toast.success('Settings saved successfully!');
        console.log('Saving settings:', { emailNotifications, theme });
        // Add API call logic here
    };

    const handleChangePassword = () => {
        if (password !== confirmPassword) {
            toast.error('Passwords do not match!');
            return;
        }
        if (password.length < 6) { // Basic validation
            toast.error('Password must be at least 6 characters long.');
            return;
        }
        // Here you would typically send password change request to your backend API
        toast.success('Password changed successfully!');
        console.log('Changing password...');
        setPassword('');
        setConfirmPassword('');
        // Add API call logic here
    };

    return (
        <div className="space-y-6">
            <h1 className="text-3xl font-bold text-gray-900">Settings</h1>

            {/* General Settings Card */}
            <Card>
                <CardHeader>
                    <CardTitle>General Settings</CardTitle>
                    <CardDescription>Manage your preferences and notifications.</CardDescription>
                </CardHeader>
                <CardContent className="space-y-4">
                    <div>
                        <Label htmlFor="emailNotifications" className="flex items-center space-x-2">
                            <input
                                type="checkbox"
                                id="emailNotifications"
                                checked={emailNotifications}
                                onChange={(e) => setEmailNotifications(e.target.checked)}
                                className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                            />
                            <span>Enable email notifications</span>
                        </Label>
                    </div>
                    <div>
                        <Label htmlFor="theme">App Theme</Label>
                        <select
                            id="theme"
                            value={theme}
                            onChange={(e) => setTheme(e.target.value)}
                            className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
                        >
                            <option value="light">Light</option>
                            <option value="dark">Dark</option>
                        </select>
                    </div>
                    <Button onClick={handleSaveChanges}>Save Changes</Button>
                </CardContent>
            </Card>

            {/* Change Password Card */}
            <Card>
                <CardHeader>
                    <CardTitle>Change Password</CardTitle>
                    <CardDescription>Update your account password.</CardDescription>
                </CardHeader>
                <CardContent className="space-y-4">
                    <div>
                        <Label htmlFor="password">New Password</Label>
                        <Input
                            id="password"
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="mt-1 block w-full"
                            placeholder="Enter new password"
                        />
                    </div>
                    <div>
                        <Label htmlFor="confirmPassword">Confirm New Password</Label>
                        <Input
                            id="confirmPassword"
                            type="password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            className="mt-1 block w-full"
                            placeholder="Confirm new password"
                        />
                    </div>
                    <Button onClick={handleChangePassword}>Change Password</Button>
                </CardContent>
            </Card>

            {/* Other potential settings sections (e.g., Profile, Privacy) can be added here */}
        </div>
    );
};

export default SettingsPage;