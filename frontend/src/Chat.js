import React, { useState, useRef, useEffect } from 'react';
import api from './api';
import ChatSidebar from './ChatSidebar';
import ReactMarkdown from 'react-markdown';
import { useNavigate } from 'react-router-dom';

function Chat() {
  const navigate = useNavigate();
  const [input, setInput] = useState('');
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(false);
  const messagesEndRef = useRef(null);
  const [sessionId, setSessionId] = useState(null);
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const handleSend = async () => {
    if (!input.trim()) return;

    const userMessage = { id: Date.now(), sender: 'user', text: input };
    setMessages([...messages, userMessage]);
    setInput('');
    setLoading(true);

    try {
      const response = await api.post('/chat/request', { message: input, chatSessionId: sessionId });
      const aiMessage = { id: Date.now() + 1, sender: 'ai', text: response.data.response };
      setMessages(prev => [...prev, aiMessage]);
      setSessionId(response.data.chatSessionId);
      setRefreshTrigger(prev => prev + 1);
    } catch (error) {
      console.error('Error:', error);
      const errorMessage = { id: Date.now() + 1, sender: 'ai', text: 'Something went wrong. Please try again.' };
      setMessages(prev => [...prev, errorMessage]);
    } finally {
      setLoading(false);
    }
  };

  const handleSelectSession = async (selectedSessionId) => {
    setLoading(true);
    try {
      const response = await api.get(`/chat/sessions/${selectedSessionId}`);
      const loadedMessages = response.data.map((msg, index) => ({
        id: index,
        sender: msg.sender === 'USER' ? 'user' : 'ai',
        text: msg.content
      }));
      setMessages(loadedMessages);
      setSessionId(selectedSessionId);
    } catch (error) {
      console.error('Error loading session:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleNewChat = () => {
    setMessages([]);
    setSessionId(null);
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

    return (
      <div className="chat-layout">
        <aside className="sidebar">
          <h1 className="brand">Learnix</h1>
          <button className="btn-new" onClick={handleNewChat}>New conversation</button>
          <ChatSidebar onSelectSession={handleSelectSession} refreshTrigger={refreshTrigger} activeSessionId={sessionId} />
          <button className="btn-logout" onClick={handleLogout}>Sign out</button>
        </aside>

        <main className="chat-main">
          <div className="chat-scroll">
            <div className="chat-inner">
              {messages.length === 0 && !loading && (
                <div className="chat-empty">
                  <h2>Ask about anything you're studying</h2>
                  <p>Your conversations are saved and you can pick them up later.</p>
                </div>
              )}

              {messages.map((msg) => (
                <div key={msg.id} className={`msg msg-${msg.sender}`}>
                  <p className="msg-label">{msg.sender === 'user' ? 'You' : 'Assistant'}</p>
                  {msg.sender === 'ai' ? (
                    <div className="msg-body"><ReactMarkdown>{msg.text}</ReactMarkdown></div>
                  ) : (
                    <p className="msg-body">{msg.text}</p>
                  )}
                </div>
              ))}

              {loading && <p className="thinking">Thinking…</p>}
              <div ref={messagesEndRef} />
            </div>
          </div>

          <div className="composer">
            <div className="composer-inner">
              <input
                value={input}
                placeholder="Ask a question…"
                onChange={(e) => setInput(e.target.value)}
                onKeyDown={(e) => { if (e.key === 'Enter') handleSend(); }}
              />
              <button onClick={handleSend}>Send</button>
            </div>
          </div>
        </main>
      </div>
    );
}

export default Chat;