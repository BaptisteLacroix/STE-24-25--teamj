import './App.css'
import { PolyfoodHeader } from './components/header'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import { RestaurantCards } from './components/restaurants';
import { RestaurantComponent } from './components/restaurant';
import { CreateGroupOrder } from './components/createGroupOrder';

const router = createBrowserRouter([
    {
        path: '/',
        element: <RestaurantCards />
    },
    {
        path: '/restaurant/:id',
        element: <RestaurantComponent />
    },
    {
        path: '/new-group-order',
        element: <CreateGroupOrder />
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
