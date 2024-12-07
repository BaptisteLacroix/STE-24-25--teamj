import { Dish, Restaurant } from "./types"


const mockRestaurants: (Restaurant & {dishes: Dish[]})[]= [
    {
        id: "1",
        name: "mock1",
        price: 4,
        timeInMinutes: 6,
        dishes: [
            {
                id: "1",
                name: "pastashiuta",
                price: 7,
                timeInMinutes: 3
            },
            {
                id: "2",
                name: "pizza",
                price: 8,
                timeInMinutes: 10
            }
        ]
    },
    {
        id: "2",
        name: "mock2",
        price: 5,
        timeInMinutes: 8,
        dishes: [
            {
                id: "3",
                name: "burger",
                price: 6,
                timeInMinutes: 5
            },
            {
                id: "4",
                name: "salad",
                price: 4,
                timeInMinutes: 2
            },
            {
                id: "5",
                name: "sushi",
                price: 10,
                timeInMinutes: 15
            },
            {
                id: "6",
                name: "ramen",
                price: 9,
                timeInMinutes: 20
            }
        ]
    },
]  

export async function getAllRestaurants(): Promise<Restaurant[]> {
    return mockRestaurants;
}

export async function getDishes(restautantId: string): Promise<Dish[]> {
    const restaurant = (mockRestaurants.filter(res=>res.id === restautantId)[0]);
    if (restaurant != undefined) return restaurant.dishes
    return []
}