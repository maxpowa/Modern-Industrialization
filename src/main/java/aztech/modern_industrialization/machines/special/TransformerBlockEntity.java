/*
 * MIT License
 *
 * Copyright (c) 2020 Azercoco & Technici4n
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package aztech.modern_industrialization.machines.special;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.api.energy.EnergyApi;
import aztech.modern_industrialization.api.energy.EnergyExtractable;
import aztech.modern_industrialization.api.energy.EnergyInsertable;
import aztech.modern_industrialization.machines.impl.MachineBlockEntity;
import aztech.modern_industrialization.machines.impl.MachineFactory;

public class TransformerBlockEntity extends MachineBlockEntity {
    private final EnergyInsertable insertable;
    private final EnergyExtractable extractable;
    private final long maxStoredEu;
    private final CableTier outputTier;

    public TransformerBlockEntity(MachineFactory factory, CableTier inputTier, CableTier outputTier) {
        super(factory);

        insertable = buildInsertable(inputTier);
        extractable = buildExtractable(outputTier);
        maxStoredEu = Math.min(inputTier.getMaxInsert(), outputTier.getMaxInsert()) * 10;
        this.outputTier = outputTier;
    }

    @Override
    protected long getMaxStoredEu() {
        return maxStoredEu;
    }

    @Override
    public void tick() {
        if (world.isClient)
            return;
        super.tick();
        autoExtractEnergy(outputDirection, outputTier);
        markDirty();
    }

    @Override
    public void registerApis() {
        EnergyApi.MOVEABLE.registerForBlockEntities((blockEntity, direction) -> {
            TransformerBlockEntity be = ((TransformerBlockEntity) blockEntity);
            return direction == be.outputDirection ? be.extractable : be.insertable;
        }, getType());
    }
}
