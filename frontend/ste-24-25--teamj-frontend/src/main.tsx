import ReactDOM from 'react-dom/client'
import {NextUIProvider} from '@nextui-org/react'
import App from './App'
import './index.css'
import {AppStateProvider} from "./modules/AppStateContext.tsx";
import {BrowserRouter} from "react-router-dom";

ReactDOM.createRoot(document.getElementById('root')!).render(
    <AppStateProvider>
        <NextUIProvider>
            <BrowserRouter>
                <App/>
            </BrowserRouter>
        </NextUIProvider>
    </AppStateProvider>,
)
