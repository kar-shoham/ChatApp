import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { userAPI, authAPI } from '../services/api';
import '../styles/Profile.css';

const Profile = () => {
  const [user, setUser] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [name, setName] = useState('');
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    loadUserProfile();
  }, []);

  const loadUserProfile = async () => {
    try {
      setLoading(true);
      const users = await userAPI.getAllUsers();
      const currentUsername = localStorage.getItem('username');
      const currentUser = users.find((u) => u.username === currentUsername);

      if (currentUser) {
        setUser(currentUser);
        setName(currentUser.name || '');
      } else {
        setError('User not found');
      }
    } catch (err) {
      setError(err.message || 'Failed to load profile');
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async () => {
    if (!user) return;

    try {
      setSaving(true);
      setError('');
      const updatedUser = await userAPI.updateUser(user.id, name);
      setUser(updatedUser);
      setIsEditing(false);
    } catch (err) {
      setError(err.message || 'Failed to update profile');
    } finally {
      setSaving(false);
    }
  };

  const handleCancel = () => {
    setName(user.name || '');
    setIsEditing(false);
    setError('');
  };

  const handleLogout = () => {
    authAPI.logout();
    navigate('/');
  };

  if (loading) {
    return (
      <div className="profile-container">
        <div className="loading">Loading profile...</div>
      </div>
    );
  }

  if (!user) {
    return (
      <div className="profile-container">
        <div className="error-message">{error || 'User not found'}</div>
      </div>
    );
  }

  return (
    <div className="profile-container">
      <div className="profile-header">
        <h2>Profile</h2>
        <button onClick={() => navigate('/groups')} className="back-btn">
          Back to Groups
        </button>
      </div>

      <div className="profile-card">
        <div className="profile-field">
          <label>Username</label>
          <div className="field-value">{user.username}</div>
        </div>

        <div className="profile-field">
          <label>Name</label>
          {isEditing ? (
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="edit-input"
              autoFocus
            />
          ) : (
            <div className="field-value">{user.name || 'Not set'}</div>
          )}
        </div>

        <div className="profile-field">
          <label>Member Since</label>
          <div className="field-value">
            {new Date(user.createdOn).toLocaleDateString()}
          </div>
        </div>

        <div className="profile-field">
          <label>Groups Joined</label>
          <div className="field-value">{user.groupIds?.length || 0}</div>
        </div>

        {error && <div className="error-message">{error}</div>}

        <div className="profile-actions">
          {isEditing ? (
            <>
              <button onClick={handleSave} disabled={saving} className="save-btn">
                {saving ? 'Saving...' : 'Save'}
              </button>
              <button onClick={handleCancel} disabled={saving} className="cancel-btn">
                Cancel
              </button>
            </>
          ) : (
            <>
              <button onClick={() => setIsEditing(true)} className="edit-btn">
                Edit Name
              </button>
              <button onClick={handleLogout} className="logout-btn">
                Logout
              </button>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default Profile;
