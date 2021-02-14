import { render, screen } from '@testing-library/react';
import App from './App';

test('renders Swagger UI link', () => {
  render(<App />);
  const linkElement = screen.getByText(/Swagger UI/i);
  expect(linkElement).toBeInTheDocument();
});
