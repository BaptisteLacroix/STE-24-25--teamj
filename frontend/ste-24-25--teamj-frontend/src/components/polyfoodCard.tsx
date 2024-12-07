import { Button } from "@nextui-org/react"

type CardProps = {
    name: string
    averageTimeInMinutes?: number
    averagePrice?: number
    imageSRC?: string
}

type FoodCardProps = CardProps & {
    type: "food"
    onAddToOrder?: () => void
}

type RestaurantCardProps = CardProps & {
    type: "restaurant"
}

export function PolyFoodCard(props: FoodCardProps | RestaurantCardProps) {
    return (
        <div className="drop-shadow-md cursor-pointer">
            <div className="bg-gray-800 text-white rounded-lg overflow-hidden w-72">
                <div className="bg-white text-black px-6 py-4 flex items-center">
                    <img src={props.imageSRC ?? "src/assets/broken_image.svg"} alt={props.imageSRC ?? "src/assets/broken_image.svg"} className="w-10 mr-3" />
                    <h3 className="text-lg font-bold">{props.name}</h3>
                </div>
                <div className="px-6 py-4 flex justify-between items-center">
                    <div className="flex justify-between text-sm w-2/3 gap-4">
                        <div className="flex justify-between">
                            <img className="mr-2" width="15" src="src/assets/time.svg" alt="time" />
                            <p>{props.type == "restaurant" && "~"} {props.averageTimeInMinutes ?? "?"} min</p>
                        </div>
                        <div className="flex justify-between">
                            <img className="mr-2" width="15" src="src/assets/money.svg" alt="price" />
                            <p>{props.type == "restaurant" && "~"} {props.averagePrice ?? "?"} €</p>
                        </div>
                    </div>
                    {
                        props.type == "food" &&
                        <Button isIconOnly radius="full" onClick={props.onAddToOrder}>
                            <img src="src/assets/add.svg" alt="shopping-cart-icon" />
                        </Button>
                    }
                </div>

            </div>
        </div>
    )
}