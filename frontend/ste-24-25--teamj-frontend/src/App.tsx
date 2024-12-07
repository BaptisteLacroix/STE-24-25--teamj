import './App.css'
import { PolyfoodHeader } from './components/header'
import { createBrowserRouter, RouterProvider } from 'react-router'
import { RestaurantCards } from './components/restaurantCards';
import { Restaurant } from './components/restaurant';

const router = createBrowserRouter([
    {
        path: '/restaurants',
        Component: RestaurantCards
    },
    {
        path: '/restaurant',
        Component: Restaurant
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
