import { Button, Modal, ModalHeader, ModalBody, ModalFooter, ModalContent } from "@nextui-org/react";
import { useState } from "react";

type copyModalProps = {
    text: string
    onCancel: () => void
}
export function CopyModal(props: copyModalProps) {
    const [text] = useState(props.text);
    const [copied, setCopied] = useState(false);

    const handleCopy = () => {
        navigator.clipboard.writeText(text)
            .then(() => {
                setCopied(true);
                setTimeout(() => setCopied(false), 2000);
            })
            .catch((err) => {
                console.error("Erreur lors de la copie : ", err);
            });
    };

    return (
        <Modal
            isOpen={true}
            aria-labelledby="modal-title"
            className="p-6"
            onClose={props.onCancel} // Added onClose handler
        >
            <ModalContent >
                <ModalHeader>
                    <h2 className="text-lg font-bold text-center">Group Order created, invite others with this code</h2>
                </ModalHeader>
                <ModalBody>
                    <p className="mb-4 text-gray-700 break-words">{text}</p>
                    <Button onClick={handleCopy} className="bg-green-500 text-white hover:bg-green-600">
                        Copy text
                    </Button>
                    {copied && <p className="mt-2 text-green-500">Copied in clipboard</p>}
                </ModalBody>
                <ModalFooter>
                    <Button variant={"bordered"} color={"danger"} onClick={props.onCancel}>
                        Close
                    </Button>
                </ModalFooter>
            </ModalContent>

        </Modal>
    );
};

export default CopyModal;
