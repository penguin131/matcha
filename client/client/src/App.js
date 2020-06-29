import React from 'react';
import './App.css';
import ErrorBoundary from './components/ErrorBoundary/ErrorBoundary'
import Header from './components/Header/Header'
function App() {
 
  return (
    <ErrorBoundary>
      <div className="App">
        <Header/>
      </div>
    </ErrorBoundary>
  );
}

export default App;
