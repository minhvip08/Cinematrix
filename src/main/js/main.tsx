import 'vite/modulepreload-polyfill';
import App from './App.tsx';
import * as ReactDOM from 'react-dom/client';
import { BrowserRouter } from "react-router-dom";

ReactDOM.createRoot(document.getElementById('root')!).render(
    <BrowserRouter>
        <App />
    </BrowserRouter>,
);