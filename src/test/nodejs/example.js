import { Blockchain } from '@ethereumjs/blockchain'
import { Chain, Common, Hardfork } from '@ethereumjs/common'
import { EEI } from '@ethereumjs/vm'
import { EVM } from '@ethereumjs/evm'
import { DefaultStateManager } from '@ethereumjs/statemanager'
import fs from 'fs'
import readline from 'readline'

const main = async () => {
    const common = new Common({ chain: Chain.Mainnet, hardfork: Hardfork.London })
    const stateManager = new DefaultStateManager()
    const blockchain = await Blockchain.create()
    const eei = new EEI(stateManager, common, blockchain)
    const byteLength = 32;

    const evm = new EVM({
        common,
        eei,
    })

    const code= fs.readFileSync('bytecode', 'utf8')
    const rl = readline.createInterface({
        input: process.stdin,
        output: process.stdout
    })

    rl.question('Input 32byte numbers: ', input => {
        console.log(input)
        let numbers = input
            .split(' ')
            .map(Number)

        const inputBytes = Buffer.alloc(byteLength * numbers.length)
        for(let i = 0; i < numbers.length; ++i) {
            const inputBytesArray = Buffer.alloc(byteLength)
            inputBytesArray.writeUInt32BE(numbers[i], byteLength - 4)

            for(let j = 0; j < inputBytesArray.length; ++j) {
                inputBytes[i * byteLength + j] = inputBytesArray[j];
            }
        }
        let buffer = Buffer.from(inputBytes, "hex");

        evm.events.on('step', function (data) {
            console.log(`Opcode: ${data.opcode.name}\tStack: ${data.stack}`)
        })

        evm
            .runCode({
                code: Buffer.from(code, 'hex'),
                gasLimit: BigInt(0xffff),
                // data : buffer
            })
            .then((results) => {
                console.log(`Returned in HEX: ${results.returnValue.toString('hex')}`)
                console.log(`Returned in DEC: ${parseInt(results.returnValue.toString('hex'), 16).toString()}`)
                console.log(`gasUsed: ${results.executionGasUsed.toString()}`)
            })
            .catch(console.error)
    })
}

void main()