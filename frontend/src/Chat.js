import React, { useState, useRef, useEffect } from 'react';
import api from './api';

function Chat() {
  const [input, setInput] = useState('');
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(false);
  const messagesEndRef = useRef(null);

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
      const response = await api.post('/chat/request', { message: input});
      const aiMessage = { id: Date.now() + 1, sender: 'ai', text: response.data.response };
      setMessages(prev => [...prev, aiMessage]);
    } catch (error) {
      console.error('Error:', error);
      const errorMessage = { id: Date.now() + 1, sender: 'ai', text: 'Something went wrong. Please try again.' };
      setMessages(prev => [...prev, errorMessage]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h1>Chat</h1>
      <div>
        {messages.map((msg) => (
          <p key={msg.id}>
            <strong>{msg.sender === 'user' ? 'You' : 'AI'}:</strong> {msg.text}
          </p>
        ))}
        {loading && <p><em>AI is thinking...</em></p>}
        <div ref={messagesEndRef} />
      </div>
      <input
        value={input}
        placeholder="Type your message..."
        onChange={(e) => setInput(e.target.value)}
      />
      <button onClick={handleSend}>Send</button>
    </div>
  );
}

export default Chat;