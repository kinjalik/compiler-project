import { Blockchain } from '@ethereumjs/blockchain'
import { Chain, Common, Hardfork } from '@ethereumjs/common'
import { EEI } from '@ethereumjs/vm'
import { EVM } from '@ethereumjs/evm'
import { DefaultStateManager } from '@ethereumjs/statemanager'
import fs from 'fs'

const main = async () => {
    const common = new Common({ chain: Chain.Mainnet, hardfork: Hardfork.London })
    const stateManager = new DefaultStateManager()
    const blockchain = await Blockchain.create()
    const eei = new EEI(stateManager, common, blockchain)

    const evm = new EVM({
        common,
        eei,
    })

    let code = ""
    try {
        code = fs.readFileSync('prog.fs', 'utf8');
    } catch (err) {
        console.error(err);
    }

    evm.events.on('step', function (data) {
        console.log(`Opcode: ${data.opcode.name}\tStack: ${data.stack}`)
    })

    evm
        .runCode({
            code: Buffer.from(code, 'hex'),
            gasLimit: BigInt(0xffff),
        })
        .then((results) => {
            console.log(`Returned: ${results.returnValue.toString('hex')}`)
            console.log(`gasUsed: ${results.executionGasUsed.toString()}`)
        })
        .catch(console.error)
}

void main()