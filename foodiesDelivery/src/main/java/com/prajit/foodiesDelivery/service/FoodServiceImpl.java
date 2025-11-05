package com.prajit.foodiesDelivery.service;

import com.prajit.foodiesDelivery.entity.FoodEntity;
import com.prajit.foodiesDelivery.io.FoodRequest;
import com.prajit.foodiesDelivery.io.FoodResponse;
import com.prajit.foodiesDelivery.repository.FoodRepository;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectAclRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class FoodServiceImpl implements FoodService{

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Autowired
    private FoodRepository foodRepository;

    @Override
    public String uploadFile(MultipartFile file) {
        String filenameExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        String key = UUID.randomUUID().toString()+"."+filenameExtension;
        try{
            PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(key).contentType(file.getContentType()).build();
            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            if(response.sdkHttpResponse().isSuccessful()){
                return "https://"+bucketName+".s3.amazonaws.com/"+key;
            }
            else{
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "file upload failed");
            }

        } catch (Exception e) {
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "an error occured while uploading the file");
        }
    }

    @Override
    public FoodResponse addFood(FoodRequest request, MultipartFile file) {
        FoodEntity newfoodEntity = convertToEntity(request);
        String imageUrl = uploadFile(file);
        newfoodEntity.setImageUrl(imageUrl);
        newfoodEntity = foodRepository.save(newfoodEntity);
        return convertToResponse(newfoodEntity);
    }

    @Override
    public List<FoodResponse> readFoods() {
        List<FoodEntity> foods = foodRepository.findAll();
        return foods.stream().map(obj->convertToResponse(obj)).collect(Collectors.toList());
    }

    @Override
    public FoodResponse readFood(String id) {
        FoodEntity food = foodRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dish not found for given Id"));
        return convertToResponse(food);
    }


    private FoodEntity convertToEntity(FoodRequest req){
        return FoodEntity.builder()
                .name(req.getName())
                .category(req.getCategory())
                .price(req.getPrice())
                .description(req.getDescription())
                .build();
    }

    private FoodResponse convertToResponse(FoodEntity entity){
         return FoodResponse.builder()
                 .id(entity.getId())
                 .name(entity.getName())
                 .price(entity.getPrice())
                 .description(entity.getDescription())
                 .imageUrl(entity.getImageUrl())
                 .category(entity.getCategory())
                 .build();
    }
}
