import {DeliveryDetails, DeliveryLocation, IndividualOrder, Order, OrderStatus, Restaurant} from "../utils/types.ts";
import {API_BASE_URL} from "../utils/apiUtils.ts";

export class RestaurantModel {

    public async getRestaurantsTypes(): Promise<string[]> {
        const response = await fetch(`${API_BASE_URL}/restaurants/types`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            return []
        }
        const data = await response.json()
        return data
    }

    public async getAllRestaurants(): Promise<Restaurant[]> {
        const response = await fetch(`${API_BASE_URL}/restaurants/all`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            return []
        }
        const data = await response.json();
        // Transform the data into the format we need
        return data.map((restaurant: any) => ({
            id: restaurant.id,
            name: restaurant.restaurantName,
            dishes: restaurant.menu.items.map((item: any) => ({
                id: item.id,
                name: item.name,
                prepTime: item.prepTime,
                price: item.price,
                type: item.type
            })),
            openingHours: restaurant.openingTime ? this.parseLocalDateTimeArray(restaurant.openingTime) : null,
            closingHours: restaurant.closingTime ? this.parseLocalDateTimeArray(restaurant.closingTime) : null
        }));
    }

    public async getRestaurantById(id: string): Promise<Restaurant | null> {
        const response = await fetch(`${API_BASE_URL}/restaurants/${id}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            return null;
        }
        const restaurant = await response.json()
        // Transform the data into the format we need
        return {
            id: restaurant.id,
            name: restaurant.restaurantName,
            dishes: restaurant.menu.items.map((item: any) => ({
                id: item.id,
                name: item.name,
                prepTime: item.prepTime,
                price: item.price,
                type: item.type
            })),
            openingHours: restaurant.openingTime ? this.parseLocalDateTimeArray(restaurant.openingTime) : null,
            closingHours: restaurant.closingTime ? this.parseLocalDateTimeArray(restaurant.closingTime) : null
        }
    }

    public async getRestaurantByFoodType(foodType: string): Promise<Restaurant[]> {
        const response = await fetch(`${API_BASE_URL}/restaurants/foodType/${foodType}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            return []
        }
        const data = await response.json()
        // Transform the data into the format we need
        return data.map((restaurant: any) => ({
            id: restaurant.id,
            name: restaurant.restaurantName,
            dishes: restaurant.menu.items.map((item: any) => ({
                id: item.id,
                name: item.name,
                prepTime: item.prepTime,
                price: item.price,
                type: item.type
            })),
            openingHours: restaurant.openingTime ? this.parseLocalDateTimeArray(restaurant.openingTime) : null,
            closingHours: restaurant.closingTime ? this.parseLocalDateTimeArray(restaurant.closingTime) : null
        }));
    }

    public async getRestaurantsByName(name: string): Promise<Restaurant[]> {
        const response = await fetch(`${API_BASE_URL}/restaurants/name/${name}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            return []
        }
        const data = await response.json()
        // Transform the data into the format we need
        return data.map((restaurant: any) => ({
            id: restaurant.id,
            name: restaurant.restaurantName,
            dishes: restaurant.menu.items.map((item: any) => ({
                id: item.id,
                name: item.name,
                prepTime: item.prepTime,
                price: item.price,
                type: item.type
            })),
            openingHours: restaurant.openingTime ? this.parseLocalDateTimeArray(restaurant.openingTime) : null,
            closingHours: restaurant.closingTime ? this.parseLocalDateTimeArray(restaurant.closingTime) : null
        }));
    }

    public async startNewOrder(userId: string, restaurantId: string, groupOrderId: string | null, deliveryDetails: DeliveryDetails | null): Promise<string> {
        /*
        const response = await fetch(`${API_BASE_URL}/${userId}/restaurants/${restaurantId}/order`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userId
            })
        })
        if (response.status !== 200) {
            throw new Error('Failed to start new order')
        }
        const data = await response.json()
        return data.orderId*/
        return "1"
    }

    public async addItemToOrder(userId: string, restaurantId: string, orderId: string, dishId: string, groupOrderId: string | null, deliveryDetails: DeliveryDetails | null): Promise<void> {
        /*
        const body = JSON.stringify({
            deliveryTime: deliveryDetails?.deliveryTime || null,
            groupOrderId: groupOrderId
        })
        const response = await fetch(`${API_BASE_URL}/${userId}/restaurants/${restaurantId}/orders/${orderId}/item/${dishId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: body
        })
        if (response.status !== 200) {
            throw new Error('Failed to add item to order')
        }*/
    }

    public async cancelOrder(userId: string, restaurantId: string, orderId): Promise<void> {
        /*
        const response = await fetch(`${API_BASE_URL}/${userId}/restaurants/${restaurantId}/orders/${orderId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to cancel order')
        }*/
    }

    public async getOrder(userId: string, orderId: string): Promise<Order | IndividualOrder> {
        /*
        const response = await fetch(`${API_BASE_URL}/${userId}/orders/${orderId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to fetch orders')
        }
        const data = await response.json()
        // Transform the data into the format we need
        return {
            id: data.id,
            restaurantId: data.restaurantId,
            userId: data.userId,
            items: data.items.map((item: any) => ({
                id: item.id,
                name: item.name,
                prepTime: item.prepTime,
                price: item.price
            })),
            status: data.status
        }*/
        return {
            id: "1",
            restaurantId: "1",
            userId: "1",
            items: [
                {
                    id: "1",
                    name: "pastashiuta",
                    prepTime: 3,
                    price: 7,
                    type: "pasta"
                },
                {
                    id: "2",
                    name: "pizza",
                    prepTime: 10,
                    price: 8,
                    type: "pizza"
                }
            ],
            status: OrderStatus.PENDING
        }
    }

    public async getTotalPriceOrder(restaurantId: string, orderId: string): Promise<number> {
        /*
        const response = await fetch(`${API_BASE_URL}/restaurant/${restaurantId}/orders/${orderId}/totalPrice`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to fetch orders')
        }
        const data = await response.json()
        return data.totalPrice*/
        return 15
    }

    public async getDeliveryLocation(userId: string): Promise<DeliveryLocation[]> {
        /*
        const response = await fetch(`${API_BASE_URL}/${userId}/deliveryLocation`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to fetch delivery details')
        }
        const data = await response.json()
        return {
            deliveryTime: new Date(data.deliveryTime),
            deliveryAddress: data.deliveryAddress
        }*/
        return [
            {
                id: "1",
                locationName: "Home",
                address: "1234 Home St",
            },
            {
                id: "2",
                locationName: "Work",
                address: "5678 Work",
            }
        ]
    }

    public async getDeliveryLocationById(userId: string, locationId: string): Promise<DeliveryLocation> {
        /*
        const response = await fetch(`${API_BASE_URL}/${userId}/deliveryLocation/${locationId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to fetch delivery details')
        }
        const data = await response.json()
        return {
            deliveryTime: new Date(data.deliveryTime),
            deliveryAddress: data.deliveryAddress
        }*/
        return {
            id: "1",
            locationName: "Home",
            address: "1234 Home St",
        }
    }

    public async createGroupOrder(userId: string, deliveryDetails: DeliveryDetails | null): Promise<string> {
        /*
        const response = await fetch(`${API_BASE_URL}/${userId}/groupOrder`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                deliveryTime: deliveryDetails.deliveryTime,
                deliveryLocation: deliveryDetails.deliveryLocation
            })
        })
        if (response.status !== 200) {
            throw new Error('Failed to create group order')
        }
        const data = await response.json()
        return data.groupOrderId*/
        return "1"
    }

    public async joinGroupOrder(userId: string, groupOrderId: string | null) {
        /*
        const response = await fetch(`${API_BASE_URL}/${userId}/groupOrder/${groupOrderId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status !== 200) {
            throw new Error('Failed to join group order')
        }
        const data = await response.json()
        return data.groupOrderId*/
        return "1"
    }

    private parseLocalDateTimeArray(arr: number[]): Date {
        const [year, month, day, hours, minutes, seconds, milliseconds] = arr;
        return new Date(year, month - 1, day, hours, minutes, seconds, milliseconds);
    }
}
