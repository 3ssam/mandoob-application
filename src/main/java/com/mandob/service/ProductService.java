package com.mandob.service;

import com.mandob.base.exception.ApiValidationException;
import com.mandob.base.repository.BaseRepository;
import com.mandob.base.service.MasterService;
import com.mandob.domain.Category;
import com.mandob.domain.Product;
import com.mandob.projection.Product.ProductProjection;
import com.mandob.repository.ProductRepository;
import com.mandob.request.ProductReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ProductService extends MasterService<Product> {
    //private final ProductMapper productMapper;
    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final UserService userService;

    public ProductProjection create(ProductReq req) {
        Product product = new Product();//productMapper.toEntity(req);
        product.setCreatedAt(Instant.now());
        product.setCreatedBy(userService.findById(req.getCurrentUser()));
        product = createNewProduct(req, product);
        setProductCategory(req, product);
        productRepository.save(product);
        return findById(product.getId(), ProductProjection.class);
    }

    public ProductProjection update(String productId, ProductReq req) {
        Product product = findById(productId);
        //productMapper.toEntity(req, product);
        product = createNewProduct(req, product);
        setProductCategory(req, product);
        productRepository.save(product);
        return findById(product.getId(), ProductProjection.class);
    }

    public Product createNewProduct(ProductReq req, Product product) {
        validateExpirationDate(req.getProdDate(), req.getExpiryDate());
        //saveImage(req.getPhotoUrl());
        product.setBarcode(req.getBarcode());
        product.setColor(req.getColor());
        product.setDescription(req.getDescription());
        product.setDimension(req.getDimension());
        product.setExpiryDate(req.getExpiryDate().toString());
        product.setProdDate(req.getProdDate().toString());
        product.setPrice(req.getPrice());
        product.setAmount(req.getAmount());
        product.setRemainingAmount(req.getAmount());
        //String productImage = saveImage(req.getPhotoUrl());
        product.setPhotoUrl(req.getPhotoUrl());
        product.setEnName(req.getEnName());
        product.setArName(req.getArName());
        product.setWeight(req.getWeight());
        //product.setSubCategory();
        product.setCompany(product.getCreatedBy().getCompany());
        product.setUpdatedAt(Instant.now());
        product.setUpdatedBy(userService.findById(req.getCurrentUser()));
        return product;
    }

//    public String saveImage(String image64){
//        byte[] imageByteArray = Base64.getDecoder().decode(image64);
//        FileOutputStream fileOutputStream = null;
//        String fileName = "image" + Product.count + ".jpg";
//        try {
//            Product.Increase();
//            String fileLocationResourse = new File("src\\main\\resources\\static\\images").getAbsolutePath();
//            String fileLocationTarget = getClass().getClassLoader().getResource(".").getPath();
//
//            File fileResource = new File(fileLocationResourse+"/"+fileName);
//            File fileTarget = new File(fileLocationTarget+"/static/images/"+fileName);
//
//            while (!fileResource.createNewFile()){
//                System.out.println("File already exists.");
//                fileName = "image" + Product.count + ".jpg";
//                Product.Increase();
//                fileResource = new File(fileLocationResourse+"/"+fileName);
//                fileTarget = new File(fileLocationTarget+"/static/images/"+fileName);
//            }
//
//            fileOutputStream = new FileOutputStream(fileResource);
//            fileOutputStream.write(imageByteArray);
//            fileOutputStream = new FileOutputStream(fileTarget);
//            fileOutputStream.write(imageByteArray);
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//        finally {
//            try {
//                fileOutputStream.close();
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//
//
//        return "http://java.tajertech.com/mandoob/images/" + fileName;
//    }


    private void setProductCategory(ProductReq req, Product product) {
        Category category = req.getCategory() != null ? categoryService.findById(req.getCategory()) : null;
        Category subCategory = req.getSubCategory() != null ? categoryService.findById(req.getSubCategory()) : null;
        product.setCategory(category);
        product.setSubCategory(category);
    }

    private void validateExpirationDate(LocalDate productDate, LocalDate expiryDate) {
        if (!expiryDate.isAfter(productDate))
            throw new ApiValidationException("expiration Date", "expiration date should be after production date");
    }

    public String delete(String productId) {
        Product product = findById(productId);
        if (product == null)
            throw new ApiValidationException("Product ID", "product id is not found in database");
        productRepository.delete(product);
        return "Successfully Delete";
    }


    public ProductProjection getProduct(String id) {
        ProductProjection product = productRepository.findByIdOrBarcode(id, id);
        if (product == null)
            throw new ApiValidationException("Product Id", "Product-id-is-not-vaild");
        return product;
    }


    @Override
    protected BaseRepository<Product> getRepository() {
        return productRepository;
    }

}
