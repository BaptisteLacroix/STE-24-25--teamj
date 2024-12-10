import ReactDOM from 'react-dom/client'
import {NextUIProvider} from '@nextui-org/react'
import App from './App'
import './index.css'
import {AppStateProvider} from "./modules/AppStateContext.tsx";

ReactDOM.createRoot(document.getElementById('root')!).render(
    <AppStateProvider>
        <NextUIProvider>
            <App/>
        </NextUIProvider>
    </AppStateProvider>,
)
