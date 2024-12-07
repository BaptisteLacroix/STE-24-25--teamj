import { Link } from "react-router"
import { PolyFoodCard } from "./polyfoodCard"

export function RestaurantCards() {
    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-y-10 gap-x-10 justify-items-center max-w-6xl">
            {[1, 2, 3, 4, 5, 6].map((num) => (
                <Link to={"/restaurant"}>
                    <PolyFoodCard name={`Temp restaurant ${num}`} averagePrice={10} key={num} type="restaurant" />
                </Link>
            ))}
        </div>
    )
}