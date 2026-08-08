import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import MockAdapter from 'axios-mock-adapter';
import api from './api';
import Chat from './Chat';
window.HTMLElement.prototype.scrollIntoView = jest.fn();

let mock;

beforeEach(() => {
  localStorage.setItem('token', 'fake-test-token');
  mock = new MockAdapter(api);
});

afterEach(() => {
  localStorage.clear();
  mock.restore();
});

test('renders input field and send button', () => {
  render(<Chat />);

  const input = screen.getByPlaceholderText('Type your message...');
  const button = screen.getByRole('button', { name: /send/i });

  expect(input).toBeInTheDocument();
  expect(button).toBeInTheDocument();
});

test('lets user type a message', async () => {
  render(<Chat />);

  const input = screen.getByPlaceholderText('Type your message...');
  const user = userEvent.setup();

  await user.type(input, 'Hello AI');

  expect(input.value).toBe('Hello AI');
});

test('sends message and displays AI response with auth header', async () => {
  mock.onPost('/chat/request').reply((config) => {
    expect(config.headers.Authorization).toBe('Bearer fake-test-token');
    expect(JSON.parse(config.data)).toEqual({ message: 'Hello AI', chatSessionId: null });
    return [200, { response: 'Hello human!' }];
  });

  render(<Chat />);

  const input = screen.getByPlaceholderText('Type your message...');
  const button = screen.getByRole('button', { name: /send/i });
  const user = userEvent.setup();

  await user.type(input, 'Hello AI');
  await user.click(button);

  expect(await screen.findByText(/Hello human!/i)).toBeInTheDocument();
});