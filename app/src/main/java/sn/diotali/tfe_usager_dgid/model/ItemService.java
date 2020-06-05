package sn.diotali.tfe_usager_dgid.model;

public class ItemService {
    private int id;
    private String nameService;
    private int imageService;

    public ItemService() {
    }

    public ItemService(int id, String nameService, int imageService) {
        this.id = id;
        this.nameService = nameService;
        this.imageService = imageService;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameService() {
        return nameService;
    }

    public void setNameService(String nameService) {
        this.nameService = nameService;
    }

    public int getImageService() {
        return imageService;
    }

    public void setImageService(int imageService) {
        this.imageService = imageService;
    }
}