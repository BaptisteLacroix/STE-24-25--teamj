import { Button } from "@nextui-org/react"
import { Dish, Restaurant } from "../types"

type CardProps = {
    imageSRC?: string
}

type FoodCardProps = CardProps & Dish & {
    type: "food"
    onAddToOrder?: () => void
}

type RestaurantCardProps = Restaurant & CardProps & {
    type: "restaurant"
}

export function PolyFoodCard(props: FoodCardProps | RestaurantCardProps) {
    return (
        <div className="drop-shadow-md cursor-pointer">
            <div className="bg-gray-800 text-white rounded-lg overflow-hidden w-72">
                <div className="bg-white text-black px-6 py-4 flex items-center">
                    <img
                        src={props.imageSRC ?? "/broken_image.svg"}
                        alt={props.imageSRC ?? "Broken Image"}
                        className="w-10 mr-3"
                    />
                    <h3 className="text-lg font-bold">{props.name}</h3>
                </div>
                <div className="px-6 py-4 flex justify-between items-center">
                    <div className="flex justify-between text-sm w-2/3 gap-4">
                        <div className="flex justify-between">
                            <img className="mr-2" width="15" src="/time.svg" alt="time" />
                            <p>{props.type == "restaurant" && "~"} {props.timeInMinutes ?? "?"} min</p>
                        </div>
                        <div className="flex justify-between">
                            <img className="mr-2" width="15" src="/money.svg" alt="price" />
                            <p>{props.type == "restaurant" && "~"} {props.price ?? "?"} €</p>
                        </div>
                    </div>
                    {
                        props.type == "food" &&
                        <Button isIconOnly radius="full" onClick={props.onAddToOrder}>
                            <img src="/add.svg" alt="shopping-cart-icon" />
                        </Button>
                    }
                </div>
            </div>
        </div>
    );
}
