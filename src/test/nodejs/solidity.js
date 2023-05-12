import { createRequire } from 'module';
const require = createRequire(import.meta.url);

const path = require("path");
const fs = require("fs");
const solc = require("solc");

const contractPath = path.resolve("Solidity", "contract0.sol");
const source = fs.readFileSync(contractPath, "utf8");

function compile(source, optimize) {
    let input = {
        language: "Solidity",
        sources: {
            "contract0.sol": {
                content: source
            }
        },
        settings: {
            outputSelection: {
                "*": {
                    "": ["legacyAST", "ast"],
                    "*": [
                        "evm.bytecode",
                        "evm.deployedBytecode",
                        "evm.methodIdentifiers",
                        "evm.gasEstimates",
                        "abi",
                        "userdoc",
                        "devdoc"
                    ]
                }
            },
            optimizer: {
                enabled: optimize
            }
        }
    };
    let output = JSON.parse(solc.compile(JSON.stringify(input)));
    if (output.errors) {
        console.log(output.errors[0].formattedMessage);
    }
    return output.contracts["contract0.sol"]["HelloWorld"];
}

let contract = compile(source, true);

console.log(contract.evm.bytecode.object);