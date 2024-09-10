package com.TBK.sanguinaire.client;

import com.TBK.sanguinaire.common.api.Limbs;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;

import java.util.ArrayList;
import java.util.List;

public class RenderUtil {public static ModelPart getModelPartForLimbs(Limbs limbs, HumanoidModel<?> model){
    ModelPart modelParts=null;
    switch (limbs){
        case HEAD -> {
            modelParts=model.head;
        }
        case BODY -> {
            modelParts=model.body;
        }
        case RIGHT_ARM -> {
            modelParts=model.rightArm;
        }
        case LEFT_ARM -> {
            modelParts=model.leftArm;
        }
        case RIGHT_LEG -> {
            modelParts=model.rightLeg;
        }
        case LEFT_LEG -> {
            modelParts=model.leftLeg;
        }
    }
    return modelParts;
}

    public static List<ModelPart> getListModelPart(List<Limbs> limbsList, PlayerModel<?> model){
        List<ModelPart> modelParts=new ArrayList<>();
        limbsList.forEach(e->{
            modelParts.addAll(getModelPartForLimbs(e,model));
        });
        return modelParts;
    }
    public static List<ModelPart> getModelPartForLimbs(Limbs limbs, PlayerModel<?> model){
        List<ModelPart> modelParts=new ArrayList<>();
        switch (limbs){
            case HEAD -> {
                modelParts.add(model.head);
            }
            case BODY -> {
                modelParts.add(model.body);
            }
            case RIGHT_ARM -> {
                modelParts.add(model.rightArm);
                modelParts.add(model.rightSleeve);
            }
            case LEFT_ARM -> {
                modelParts.add(model.leftArm);
                modelParts.add(model.leftSleeve);
            }
            case RIGHT_LEG -> {
                modelParts.add(model.rightLeg);
            }
            case LEFT_LEG -> {
                modelParts.add(model.leftLeg);
            }
        }
        return modelParts;
    }

}
