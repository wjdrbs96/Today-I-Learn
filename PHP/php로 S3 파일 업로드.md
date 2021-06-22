# `PHP로 S3 파일 업로드 하기`

```
<?php

ini_set("display_errors", "1");
$uploaddir = $_SERVER['DOCUMENT_ROOT'].'/test/';
$uploadfile = $uploaddir . basename($_FILES['images']['name']);

echo $uploadfile;

if (move_uploaded_file($_FILES['images']['tmp_name'], $uploadfile)) {
  echo "파일이 성공적으로 업로드 됨";
} else {
  print "파일이 잘못됨\n";
}

define('S3_KEY', 'IAM Key');
define('S3_SECRET_KEY', 'IAM Secrey Key');
define('BUCKET', 'aws-gyun-s3');
include_once('/Users/choejeong-gyun/Documents/aws/aws-autoloader.php');
// 앞서 다운로드한 SDK 파일의 autoloader를 불러옵니다.
 
use Aws\S3\S3Client;
use Aws\S3\Exception\S3Exception;
// S3 파일 업로드에 필요한 클래스를 불러옵니다.
 
$s3Client = S3Client::factory(array(
  'region' => 'ap-northeast-2',
  'version' => 'latest',
  'signature' => 'v4',
  'key'    => 'IAM Key',
  'secret' => 'IAM SecretKey'
));
// AWS IAM 에서 등록한 사용자의 Key와 Secret key로 서울 리전('ap-northeast-2')의 S3로 접근합니다.


$s3Client->putObject(array(
  'Bucket' => '버킷 이름',
  'Key'    => 'static',
  'Body'   => fopen($_SERVER['DOCUMENT_ROOT'].'/test/1212.png', 'r'),
  'ACL'    => 'public-read'
));

$conn = mysqli_connect(
  '127.0.0.1', 
  'root', 
  'root', 
  'test');
$sql = "
  INSERT INTO post
    (title, content, created_at)
    VALUES(
      '{$_POST['title']}',
      '{$_POST['content']}',
      NOW()
    )
";

$result = mysqli_query($conn, $sql);
echo mysqli_error($conn);


header( 'Location: http://localhost:8080/main.php' );
?>
```