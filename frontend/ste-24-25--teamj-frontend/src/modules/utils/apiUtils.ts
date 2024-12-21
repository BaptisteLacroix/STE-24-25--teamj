export const API_BASE_URL = "http://localhost:8080/api/gateway"

export const uuidv4 = (): string => {
    return "10000000-1000-4000-8000-100000000000".replace(/[018]/g, c =>
        (+c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> +c / 4).toString(16)
    );
}


export const toDate = (dateArray: number[]): Date => {
    // Check if the array length is 6 or 7
    if (dateArray.length === 7) {
        // Adjust for the full date with milliseconds
        return new Date(
            dateArray[0],        // Year
            dateArray[1] - 1,    // Month (0-based index)
            dateArray[2],        // Day
            dateArray[3],        // Hour
            dateArray[4],        // Minute
            dateArray[5],        // Second
            dateArray[6]         // Milliseconds
        );
    } else if (dateArray.length === 5) {
        // Adjust for the date without seconds or milliseconds
        return new Date(
            dateArray[0],        // Year
            dateArray[1] - 1,    // Month (0-based index)
            dateArray[2],        // Day
            dateArray[3],        // Hour
            dateArray[4]         // Minute
        );
    } else {
        // Handle invalid input (optional)
        throw new Error("Invalid date array format.");
    }
};
