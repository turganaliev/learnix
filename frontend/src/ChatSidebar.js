import React, { useState, useEffect } from 'react';
import api from './api';

function ChatSidebar({ onSelectSession, refreshTrigger, activeSessionId }) {
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
      return <p className="sidebar-empty">Loading…</p>;
    }

    if (sessions.length === 0) {
      return <p className="sidebar-empty">No conversations yet.</p>;
    }

    return (
      <div>
        <h3>Conversations</h3>
        <ul className="session-list">
          {sessions.map((session) => (
            <li key={session.id}>
              <button
                className={`session-item ${session.id === activeSessionId ? 'session-item-active' : ''}`}
                onClick={() => onSelectSession(session.id)}
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