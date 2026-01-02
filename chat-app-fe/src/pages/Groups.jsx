import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { groupAPI, userAPI } from '../services/api';
import '../styles/Groups.css';

const Groups = () => {
  const [groups, setGroups] = useState([]);
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [actionLoading, setActionLoading] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      setError('');

      const [allGroups, allUsers] = await Promise.all([
        groupAPI.getAllGroups(),
        userAPI.getAllUsers(),
      ]);

      const currentUsername = localStorage.getItem('username');
      const user = allUsers.find((u) => u.username === currentUsername);

      setGroups(allGroups);
      setCurrentUser(user);
    } catch (err) {
      setError(err.message || 'Failed to load groups');
    } finally {
      setLoading(false);
    }
  };

  const isUserInGroup = (groupId) => {
    return currentUser?.groupIds?.includes(groupId);
  };

  const handleJoinGroup = async (groupId) => {
    try {
      setActionLoading({ ...actionLoading, [groupId]: true });
      await groupAPI.joinGroup(groupId);
      await loadData();
    } catch (err) {
      setError(err.message || 'Failed to join group');
    } finally {
      setActionLoading({ ...actionLoading, [groupId]: false });
    }
  };

  const handleLeaveGroup = async (groupId) => {
    try {
      setActionLoading({ ...actionLoading, [groupId]: true });
      await groupAPI.leaveGroup(groupId);
      await loadData();
    } catch (err) {
      setError(err.message || 'Failed to leave group');
    } finally {
      setActionLoading({ ...actionLoading, [groupId]: false });
    }
  };

  const handleEnterChat = (groupId) => {
    navigate(`/chat/${groupId}`);
  };

  const handleRefresh = () => {
    loadData();
  };

  if (loading) {
    return (
      <div className="groups-container">
        <div className="loading">Loading groups...</div>
      </div>
    );
  }

  return (
    <div className="groups-container">
      <div className="groups-header">
        <h2>Groups</h2>
        <div className="header-actions">
          <button onClick={handleRefresh} className="refresh-btn">
            Refresh
          </button>
          <button onClick={() => navigate('/profile')} className="profile-btn">
            Profile
          </button>
        </div>
      </div>

      {error && <div className="error-message">{error}</div>}

      <div className="groups-list">
        {groups.length === 0 ? (
          <div className="no-groups">No groups available</div>
        ) : (
          groups.map((group) => {
            const isMember = isUserInGroup(group.id);
            const isLoading = actionLoading[group.id];

            return (
              <div key={group.id} className="group-card">
                <div className="group-info">
                  <h3>{group.groupName}</h3>
                  <div className="group-details">
                    <span className="group-code">Code: {group.groupCode}</span>
                    <span className="group-members">
                      {group.userIds?.length || 0} members
                    </span>
                  </div>
                  {group.createdOn && (
                    <div className="group-date">
                      Created: {new Date(group.createdOn).toLocaleDateString()}
                    </div>
                  )}
                </div>

                <div className="group-actions">
                  {isMember ? (
                    <>
                      <button
                        onClick={() => handleEnterChat(group.id)}
                        className="enter-chat-btn"
                        disabled={isLoading}
                      >
                        Enter Chat
                      </button>
                      <button
                        onClick={() => handleLeaveGroup(group.id)}
                        className="leave-btn"
                        disabled={isLoading}
                      >
                        {isLoading ? 'Leaving...' : 'Leave'}
                      </button>
                    </>
                  ) : (
                    <button
                      onClick={() => handleJoinGroup(group.id)}
                      className="join-btn"
                      disabled={isLoading}
                    >
                      {isLoading ? 'Joining...' : 'Join Group'}
                    </button>
                  )}
                </div>
              </div>
            );
          })
        )}
      </div>
    </div>
  );
};

export default Groups;
