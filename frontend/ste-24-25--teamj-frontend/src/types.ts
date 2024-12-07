export type Restaurant = {
    id: string
    name: string
    timeInMinutes?: number // per dish on average
    price?: number // per dish on average
}

export type Dish = {
    id: string
    name: string
    timeInMinutes?: number
    price: number
}