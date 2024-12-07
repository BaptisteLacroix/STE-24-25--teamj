import { Button, Input, Navbar, NavbarContent } from "@nextui-org/react";
import { SearchIcon } from "./searchIcon";

export function PolyfoodHeader() {
  return (
    <Navbar isBordered>
      <NavbarContent as="div" className="items-center flex" justify="start">
        <Button radius="full">New Group Order</Button>
        <Button radius="full">Join Group Order</Button>
      </NavbarContent>

      <NavbarContent as="div" className="items-center" justify="center">
        <Input
          classNames={{
            base: "max-w-full sm:max-w-[20rem] h-10",
            mainWrapper: "h-full",
            input: "text-small",
            inputWrapper: "h-full font-normal text-default-500 bg-default-400/20 dark:bg-default-500/20",
          }}
          placeholder="Type to search..."
          size="lg"
          startContent={<SearchIcon size={18} />}
          type="search"
        />
      </NavbarContent>
      <NavbarContent as="div" className="items-center" justify="end">
        <Button isIconOnly radius="full">
          <img src="/shopping-cart.svg" alt="shopping-cart-icon" />
        </Button>
      </NavbarContent>
    </Navbar>
  );
}

