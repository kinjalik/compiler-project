import { Blockchain } from '@ethereumjs/blockchain'
import { Chain, Common, Hardfork } from '@ethereumjs/common'
import { EEI } from '@ethereumjs/vm'
import { EVM } from '@ethereumjs/evm'
import { DefaultStateManager } from '@ethereumjs/statemanager'

// Note: in a future release there will be an EEI default implementation
// which will ease standalone initialization
const main = async () => {
    const common = new Common({ chain: Chain.Mainnet, hardfork: Hardfork.London })
    const stateManager = new DefaultStateManager()
    const blockchain = await Blockchain.create()
    const eei = new EEI(stateManager, common, blockchain)

    const evm = new EVM({
        common,
        eei,
    })

    const code = [
        "7f0000000000000000000000000000000000000000000000000000000000000040",
        "7f0000000000000000000000000000000000000000000000000000000000000000",
        "52",
        "7f0000000000000000000000000000000000000000000000000000000000000000",
        "80",
        "7f0000000000000000000000000000000000000000000000000000000000000040",
        "52",
        "7f0000000000000000000000000000000000000000000000000000000000000080",
        "52",
        "7f0000000000000000000000000000000000000000000000000000000000000000",
        "7f0000000000000000000000000000000000000000000000000000000000000060",
        "52",
        "7f000000000000000000000000000000000000000000000000000000000000010E",
        "56",
        "5b",
        "7f0000000000000000000000000000000000000000000000000000000000000000",
        "7f0000000000000000000000000000000000000000000000000000000000000000",
        "51",
        "7f0000000000000000000000000000000000000000000000000000000000000020",
        "01",
        "52",
        "7f0000000000000000000000000000000000000000000000000000000000000001",
        "7f0000000000000000000000000000000000000000000000000000000000000002",
        "01",
        "7f0000000000000000000000000000000000000000000000000000000000000000",
        "52",
        "7f0000000000000000000000000000000000000000000000000000000000000020",
        "7f0000000000000000000000000000000000000000000000000000000000000000",
        "f3",
    ]

    evm.events.on('step', function (data) {
        // Note that data.stack is not immutable, i.e. it is a reference to the vm's internal stack object
        console.log(`Opcode: ${data.opcode.name}\tStack: ${data.stack}`)
    })

    console.log(Buffer.from(code.join(''), 'hex'))

    evm
        .runCode({
            code: Buffer.from(code.join(''), 'hex'),
            gasLimit: BigInt(0xffff),
        })
        .then((results) => {
            console.log(`Returned: ${results.returnValue.toString('hex')}`)
            console.log(`gasUsed: ${results.executionGasUsed.toString()}`)
        })
        .catch(console.error)
}

void main()