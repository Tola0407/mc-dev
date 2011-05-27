package net.minecraft.server;

import java.util.Random;

public class BlockDispenser extends BlockContainer {

    private Random a = new Random();

    protected BlockDispenser(int i) {
        super(i, Material.STONE);
        this.textureId = 45;
    }

    public int c() {
        return 4;
    }

    public int a(int i, Random random) {
        return Block.DISPENSER.id;
    }

    public void e(World world, int i, int j, int k) {
        super.e(world, i, j, k);
        this.g(world, i, j, k);
    }

    private void g(World world, int i, int j, int k) {
        if (!world.isStatic) {
            int l = world.getTypeId(i, j, k - 1);
            int i1 = world.getTypeId(i, j, k + 1);
            int j1 = world.getTypeId(i - 1, j, k);
            int k1 = world.getTypeId(i + 1, j, k);
            byte b0 = 3;

            if (Block.o[l] && !Block.o[i1]) {
                b0 = 3;
            }

            if (Block.o[i1] && !Block.o[l]) {
                b0 = 2;
            }

            if (Block.o[j1] && !Block.o[k1]) {
                b0 = 5;
            }

            if (Block.o[k1] && !Block.o[j1]) {
                b0 = 4;
            }

            world.setData(i, j, k, b0);
        }
    }

    public int a(int i) {
        return i == 1 ? this.textureId + 17 : (i == 0 ? this.textureId + 17 : (i == 3 ? this.textureId + 1 : this.textureId));
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman) {
        if (world.isStatic) {
            return true;
        } else {
            TileEntityDispenser tileentitydispenser = (TileEntityDispenser) world.getTileEntity(i, j, k);

            entityhuman.a(tileentitydispenser);
            return true;
        }
    }

    private void dispense(World world, int i, int j, int k, Random random) {
        int l = world.getData(i, j, k);
        float f = 0.0F;
        float f1 = 0.0F;

        if (l == 3) {
            f1 = 1.0F;
        } else if (l == 2) {
            f1 = -1.0F;
        } else if (l == 5) {
            f = 1.0F;
        } else {
            f = -1.0F;
        }

        TileEntityDispenser tileentitydispenser = (TileEntityDispenser) world.getTileEntity(i, j, k);
        ItemStack itemstack = tileentitydispenser.b();
        double d0 = (double) i + (double) f * 0.6D + 0.5D;
        double d1 = (double) j + 0.5D;
        double d2 = (double) k + (double) f1 * 0.6D + 0.5D;

        if (itemstack == null) {
            world.makeSound((double) i, (double) j, (double) k, "random.click", 1.0F, 1.2F);
        } else {
            double d3;

            if (itemstack.id == Item.ARROW.id) {
                EntityArrow entityarrow = new EntityArrow(world, d0, d1, d2);

                entityarrow.a((double) f, 0.10000000149011612D, (double) f1, 1.1F, 6.0F);
                entityarrow.a = true;
                world.addEntity(entityarrow);
                world.makeSound((double) i, (double) j, (double) k, "random.bow", 1.0F, 1.2F);
            } else if (itemstack.id == Item.EGG.id) {
                EntityEgg entityegg = new EntityEgg(world, d0, d1, d2);

                entityegg.a((double) f, 0.10000000149011612D, (double) f1, 1.1F, 6.0F);
                world.addEntity(entityegg);
                world.makeSound((double) i, (double) j, (double) k, "random.bow", 1.0F, 1.2F);
            } else if (itemstack.id == Item.SNOW_BALL.id) {
                EntitySnowball entitysnowball = new EntitySnowball(world, d0, d1, d2);

                entitysnowball.a((double) f, 0.10000000149011612D, (double) f1, 1.1F, 6.0F);
                world.addEntity(entitysnowball);
                world.makeSound((double) i, (double) j, (double) k, "random.bow", 1.0F, 1.2F);
            } else {
                EntityItem entityitem = new EntityItem(world, d0, d1 - 0.3D, d2, itemstack);

                d3 = random.nextDouble() * 0.1D + 0.2D;
                entityitem.motX = (double) f * d3;
                entityitem.motY = 0.20000000298023224D;
                entityitem.motZ = (double) f1 * d3;
                entityitem.motX += random.nextGaussian() * 0.007499999832361937D * 6.0D;
                entityitem.motY += random.nextGaussian() * 0.007499999832361937D * 6.0D;
                entityitem.motZ += random.nextGaussian() * 0.007499999832361937D * 6.0D;
                world.addEntity(entityitem);
                world.makeSound((double) i, (double) j, (double) k, "random.click", 1.0F, 1.0F);
            }

            for (int i1 = 0; i1 < 10; ++i1) {
                d3 = random.nextDouble() * 0.2D + 0.01D;
                double d4 = d0 + (double) f * 0.01D + (random.nextDouble() - 0.5D) * (double) f1 * 0.5D;
                double d5 = d1 + (random.nextDouble() - 0.5D) * 0.5D;
                double d6 = d2 + (double) f1 * 0.01D + (random.nextDouble() - 0.5D) * (double) f * 0.5D;
                double d7 = (double) f * d3 + random.nextGaussian() * 0.01D;
                double d8 = -0.03D + random.nextGaussian() * 0.01D;
                double d9 = (double) f1 * d3 + random.nextGaussian() * 0.01D;

                world.a("smoke", d4, d5, d6, d7, d8, d9);
            }
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (l > 0 && Block.byId[l].isPowerSource()) {
            boolean flag = world.isBlockIndirectlyPowered(i, j, k) || world.isBlockIndirectlyPowered(i, j + 1, k);

            if (flag) {
                world.c(i, j, k, this.id, this.c());
            }
        }
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (world.isBlockIndirectlyPowered(i, j, k) || world.isBlockIndirectlyPowered(i, j + 1, k)) {
            this.dispense(world, i, j, k, random);
        }
    }

    protected TileEntity a_() {
        return new TileEntityDispenser();
    }

    public void postPlace(World world, int i, int j, int k, EntityLiving entityliving) {
        int l = MathHelper.floor((double) (entityliving.yaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0) {
            world.setData(i, j, k, 2);
        }

        if (l == 1) {
            world.setData(i, j, k, 5);
        }

        if (l == 2) {
            world.setData(i, j, k, 3);
        }

        if (l == 3) {
            world.setData(i, j, k, 4);
        }
    }

    public void remove(World world, int i, int j, int k) {
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser) world.getTileEntity(i, j, k);

        for (int l = 0; l < tileentitydispenser.getSize(); ++l) {
            ItemStack itemstack = tileentitydispenser.getItem(l);

            if (itemstack != null) {
                float f = this.a.nextFloat() * 0.8F + 0.1F;
                float f1 = this.a.nextFloat() * 0.8F + 0.1F;
                float f2 = this.a.nextFloat() * 0.8F + 0.1F;

                while (itemstack.count > 0) {
                    int i1 = this.a.nextInt(21) + 10;

                    if (i1 > itemstack.count) {
                        i1 = itemstack.count;
                    }

                    itemstack.count -= i1;
                    EntityItem entityitem = new EntityItem(world, (double) ((float) i + f), (double) ((float) j + f1), (double) ((float) k + f2), new ItemStack(itemstack.id, i1, itemstack.getData()));
                    float f3 = 0.05F;

                    entityitem.motX = (double) ((float) this.a.nextGaussian() * f3);
                    entityitem.motY = (double) ((float) this.a.nextGaussian() * f3 + 0.2F);
                    entityitem.motZ = (double) ((float) this.a.nextGaussian() * f3);
                    world.addEntity(entityitem);
                }
            }
        }

        super.remove(world, i, j, k);
    }
}