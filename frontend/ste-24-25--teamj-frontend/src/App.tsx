import './App.css'
import { PolyfoodHeader } from './components/header'
import { RestaurantCard } from './components/restaurantCard'

function App() {
    return (
        <div className='flex flex-col items-center'>
            <PolyfoodHeader/>
            <div className="mt-10 w-full flex justify-center">
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-y-10 gap-x-10 justify-items-center max-w-6xl">
                    {[1, 2, 3, 4, 5, 6].map((num) => (
                        <RestaurantCard name={`Temp restaurant ${num}`} averagePrice={10} key={num} />
                    ))}
                </div>
            </div>
        </div>
    )
}
 
export default App
