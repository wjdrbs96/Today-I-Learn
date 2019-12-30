package Generic;

public class GenericPrinter<T extends Material> { //Material 에서 상속 받은 애들만 사용가능
    //재료는 Power 일수도 있고, Plastic 일수도 있음
    private T material;

    public T getMaterial(){
        return material;
    }

    public void setMaterial(T material){
        this.material = material;
    }

    public String toString(){
        return material.toString();
    }

}
