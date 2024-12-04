

type RestaurantCardProps = {
    name: string
    averageTimeInMinutes?: number
    averagePrice?: number
    imageSRC?: string
}

export function RestaurantCard(props: RestaurantCardProps) {
    return (
        <div className="drop-shadow-md cursor-pointer">
            <div className="bg-gray-800 text-white rounded-lg overflow-hidden w-72">
                <div className="bg-white text-black px-6 py-4 flex items-center">
                    <img src={props.imageSRC ?? "src/assets/broken_image.svg"} alt={props.imageSRC ?? "src/assets/broken_image.svg"} className="w-10 mr-3" />
                    <h3 className="text-lg font-bold">{props.name}</h3>
                </div>
                <div className="px-6 py-4">
                    <div className="flex justify-between text-sm">
                        <p>~ {props.averageTimeInMinutes ?? "?"} min</p>
                        <p>~ {props.averagePrice ?? "?"} €</p>
                    </div>
                </div>
            </div>
        </div>
    )
}