import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './styles/main.css'

/**
 * Main entry point for React application
 * 
 * This file:
 * 1. Imports React and ReactDOM
 * 2. Imports the main App component
 * 3. Imports global CSS styles
 * 4. Renders the App component into the DOM
 * 
 * ReactDOM.createRoot() - Creates a root for React 18
 * document.getElementById('root') - Finds the div in index.html
 * .render() - Renders the React component tree
 */
ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <App />
    </React.StrictMode>,
)
