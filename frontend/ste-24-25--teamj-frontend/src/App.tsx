import './App.css'
import { PolyfoodHeader } from './components/header'
import { createBrowserRouter, RouterProvider } from 'react-router'
import { RestaurantCards } from './components/restaurants';
import { RestaurantComponent } from './components/restaurant';

const router = createBrowserRouter([
    {
        path: '/',
        element: <RestaurantCards />
    },
    {
        path: '/restaurant/:id',
        element: <RestaurantComponent />
    },
]);

function App() {
    return (
        <div className='flex flex-col items-center'>
            <PolyfoodHeader />
            <div className='mt-10'>
                <RouterProvider router={router} />
            </div>
        </div>
    )
}

export default App
