import React from 'react'

class ErrorBoundary extends React.Component {
    constructor(props) {
        super(props);
        this.state = { hasError: false };
    }

    static getDerivedStateFromError(error) {
        return { hasError: true };
    }

    render() {
        if (this.state.hasError) {
            // сделать компонент для ошибки
            return <h1>Что-то пошло не так.</h1>;
        }

        return this.props.children; 
    }
}

export default ErrorBoundary
