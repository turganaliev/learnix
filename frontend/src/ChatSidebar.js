import React, { useState, useEffect } from 'react';
import api from './api';

function ChatSidebar({ onSelectSession, refreshTrigger }) {
  const [sessions, setSessions] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchSessions = async () => {
      setLoading(true);
      try {
        const response = await api.get('/chat/sessions');
        setSessions(response.data);
      } catch (error) {
        console.error('Error fetching sessions:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchSessions();
  }, [refreshTrigger]);

  if (loading) {
    return <p>Loading conversations...</p>;
  }

  if (sessions.length === 0) {
    return <p>No conversations yet.</p>;
  }

  return (
    <div>
      <h3>Conversations</h3>
      <ul style={{ listStyle: 'none', padding: 0, margin: 0 }}>
        {sessions.map((session) => (
          <li key={session.id}>
            <button
              onClick={() => onSelectSession(session.id)}
              style={{
                width: '250px',
                textAlign: 'left',
                padding: '8px',
                marginBottom: '4px',
                cursor: 'pointer'
              }}
            >
              {session.title}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default ChatSidebar;