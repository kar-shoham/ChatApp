import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { chatAPI, groupAPI, userAPI } from '../services/api';
import '../styles/Chat.css';

const Chat = () => {
  const { groupId } = useParams();
  const navigate = useNavigate();
  const [messages, setMessages] = useState([]);
  const [group, setGroup] = useState(null);
  const [users, setUsers] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [refreshing, setRefreshing] = useState(false);
  const [newMessage, setNewMessage] = useState('');
  const [sending, setSending] = useState(false);

  useEffect(() => {
    loadChatData();
  }, [groupId]);

  const loadChatData = async () => {
    try {
      setLoading(true);
      setError('');

      const [messagesData, groupData, usersData] = await Promise.all([
        chatAPI.getGroupMessages(groupId),
        groupAPI.getGroupById(groupId),
        userAPI.getAllUsers(),
      ]);

      setMessages(messagesData);
      setGroup(groupData);

      const usersMap = {};
      usersData.forEach((user) => {
        usersMap[user.id] = user;
      });
      setUsers(usersMap);
    } catch (err) {
      setError(err.message || 'Failed to load chat');
    } finally {
      setLoading(false);
    }
  };

  const handleRefresh = async () => {
    try {
      setRefreshing(true);
      const messagesData = await chatAPI.getGroupMessages(groupId);
      setMessages(messagesData);
      setError('');
    } catch (err) {
      setError(err.message || 'Failed to refresh messages');
    } finally {
      setRefreshing(false);
    }
  };

  const handleSendMessage = async (e) => {
    e.preventDefault();

    if (!newMessage.trim()) {
      return;
    }

    try {
      setSending(true);
      setError('');
      await chatAPI.sendMessage(groupId, newMessage);
      setNewMessage('');

      const messagesData = await chatAPI.getGroupMessages(groupId);
      setMessages(messagesData);
    } catch (err) {
      setError(err.message || 'Failed to send message');
    } finally {
      setSending(false);
    }
  };

  const formatTime = (timestamp) => {
    const date = new Date(timestamp);
    return date.toLocaleString();
  };

  const getSenderName = (senderId) => {
    const user = users[senderId];
    return user ? user.name || user.username : 'Unknown';
  };

  const getCurrentUserId = () => {
    const currentUsername = localStorage.getItem('username');
    const currentUser = Object.values(users).find(
      (user) => user.username === currentUsername
    );
    return currentUser?.id;
  };

  if (loading) {
    return (
      <div className="chat-container">
        <div className="loading">Loading chat...</div>
      </div>
    );
  }

  if (!group) {
    return (
      <div className="chat-container">
        <div className="error-message">Group not found</div>
      </div>
    );
  }

  const currentUserId = getCurrentUserId();

  return (
    <div className="chat-container">
      <div className="chat-header">
        <div className="chat-header-left">
          <button onClick={() => navigate('/groups')} className="back-btn">
            Back
          </button>
          <div className="group-info">
            <h2>{group.groupName}</h2>
            <span className="member-count">
              {group.userIds?.length || 0} members
            </span>
          </div>
        </div>
        <button
          onClick={handleRefresh}
          disabled={refreshing}
          className="refresh-btn"
        >
          {refreshing ? 'Refreshing...' : 'Refresh'}
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}

      <div className="messages-container">
        {messages.length === 0 ? (
          <div className="no-messages">
            No messages yet. Be the first to send a message!
          </div>
        ) : (
          messages.map((message) => {
            const isOwnMessage = message.senderId === currentUserId;

            return (
              <div
                key={message.id}
                className={`message ${isOwnMessage ? 'own-message' : 'other-message'}`}
              >
                <div className="message-header">
                  <span className="sender-name">
                    {getSenderName(message.senderId)}
                  </span>
                  <span className="message-time">
                    {formatTime(message.createdOn)}
                  </span>
                </div>
                <div className="message-content">{message.message}</div>
                {message.modifiedOn &&
                  message.modifiedOn !== message.createdOn && (
                    <div className="message-edited">(edited)</div>
                  )}
              </div>
            );
          })
        )}
      </div>

      <div className="message-input-container">
        <form onSubmit={handleSendMessage} className="message-form">
          <input
            type="text"
            value={newMessage}
            onChange={(e) => setNewMessage(e.target.value)}
            placeholder="Type your message..."
            className="message-input"
            disabled={sending}
          />
          <button
            type="submit"
            className="send-btn"
            disabled={sending || !newMessage.trim()}
          >
            {sending ? 'Sending...' : 'Send'}
          </button>
        </form>
        <div className="chat-info">
          <p>Note: Click refresh to see new messages from other users</p>
        </div>
      </div>
    </div>
  );
};

export default Chat;
